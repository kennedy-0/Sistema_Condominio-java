package br.edu.poo.m5678;

import br.edu.poo.m5678.config.ConexaoDB;
import br.edu.poo.m5678.view.Menu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            ConexaoDB.inicializar();
        } catch (SQLException e) {
            System.out.println("Não foi possível conectar ao MySQL: " + e.getMessage());
            System.out.println("Verifique se o MySQL está rodando e se o usuário/senha do .env estão corretos.");
            return;
        }
        new Menu().iniciar();
    }
}
