package br.edu.poo.m5678.dao;

import br.edu.poo.m5678.config.ConexaoDB;
import br.edu.poo.m5678.model.Morador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MoradorDAO {

    private static final String SELECT_BASE =
            "SELECT m.*, CONCAT('Bloco ', a.bloco, ' - Apto ', a.numero) AS apto_desc "
                    + "FROM morador m JOIN apartamento a ON a.id = m.apartamento_id ";

    public void inserir(Morador m) throws SQLException {
        String sql = "INSERT INTO morador (nome, cpf, telefone, email, cep, logradouro, bairro, cidade, uf, apartamento_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, m);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Morador> listar() throws SQLException {
        List<Morador> lista = new ArrayList<>();
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + "ORDER BY m.nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Morador> listarPorApartamento(int apartamentoId) throws SQLException {
        List<Morador> lista = new ArrayList<>();
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE m.apartamento_id = ? ORDER BY m.nome")) {
            ps.setInt(1, apartamentoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public Morador buscarPorId(int id) throws SQLException {
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE m.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean atualizar(Morador m) throws SQLException {
        String sql = "UPDATE morador SET nome = ?, cpf = ?, telefone = ?, email = ?, cep = ?, logradouro = ?, "
                + "bairro = ?, cidade = ?, uf = ?, apartamento_id = ? WHERE id = ?";
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            preencher(ps, m);
            ps.setInt(11, m.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM morador WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int contarPorApartamento(int apartamentoId) throws SQLException {
        try (Connection c = ConexaoDB.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM morador WHERE apartamento_id = ?")) {
            ps.setInt(1, apartamentoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private void preencher(PreparedStatement ps, Morador m) throws SQLException {
        ps.setString(1, m.getNome());
        ps.setString(2, m.getCpf());
        ps.setString(3, m.getTelefone());
        ps.setString(4, m.getEmail());
        ps.setString(5, m.getCep());
        ps.setString(6, m.getLogradouro());
        ps.setString(7, m.getBairro());
        ps.setString(8, m.getCidade());
        ps.setString(9, m.getUf());
        ps.setInt(10, m.getApartamentoId());
    }

    private Morador mapear(ResultSet rs) throws SQLException {
        Morador m = new Morador();
        m.setId(rs.getInt("id"));
        m.setNome(rs.getString("nome"));
        m.setCpf(rs.getString("cpf"));
        m.setTelefone(rs.getString("telefone"));
        m.setEmail(rs.getString("email"));
        m.setCep(rs.getString("cep"));
        m.setLogradouro(rs.getString("logradouro"));
        m.setBairro(rs.getString("bairro"));
        m.setCidade(rs.getString("cidade"));
        m.setUf(rs.getString("uf"));
        m.setApartamentoId(rs.getInt("apartamento_id"));
        m.setApartamentoDescricao(rs.getString("apto_desc"));
        return m;
    }
}
