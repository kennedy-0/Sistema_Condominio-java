package br.edu.poo.m5678.dao;

import br.edu.poo.m5678.config.ConexaoDB;
import br.edu.poo.m5678.model.Apartamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ApartamentoDAO {

    public void inserir(Apartamento a) throws SQLException {
        String sql = "INSERT INTO apartamento (numero, bloco, andar) VALUES (?, ?, ?)";
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNumero());
            ps.setString(2, a.getBloco());
            ps.setInt(3, a.getAndar());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Apartamento> listar() throws SQLException {
        String sql = "SELECT id, numero, bloco, andar FROM apartamento ORDER BY bloco, numero";
        List<Apartamento> lista = new ArrayList<>();
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Apartamento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, numero, bloco, andar FROM apartamento WHERE id = ?";
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean atualizar(Apartamento a) throws SQLException {
        String sql = "UPDATE apartamento SET numero = ?, bloco = ?, andar = ? WHERE id = ?";
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNumero());
            ps.setString(2, a.getBloco());
            ps.setInt(3, a.getAndar());
            ps.setInt(4, a.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM apartamento WHERE id = ?";
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Apartamento mapear(ResultSet rs) throws SQLException {
        Apartamento a = new Apartamento();
        a.setId(rs.getInt("id"));
        a.setNumero(rs.getString("numero"));
        a.setBloco(rs.getString("bloco"));
        a.setAndar(rs.getInt("andar"));
        return a;
    }
}
