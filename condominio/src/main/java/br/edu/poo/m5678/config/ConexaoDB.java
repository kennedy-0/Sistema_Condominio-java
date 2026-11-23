package br.edu.poo.m5678.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {

    private static final Dotenv ENV = Dotenv.configure().ignoreIfMissing().load();

    private ConexaoDB() {
    }

    public static Connection getConexao() throws SQLException {
        String host = ENV.get("DB_HOST", "localhost");
        String port = ENV.get("DB_PORT", "3306");
        String db = ENV.get("DB_NAME", "condominio");
        String user = ENV.get("DB_USER", "root");
        String pass = ENV.get("DB_PASSWORD", "");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db
                + "?createDatabaseIfNotExist=true&serverTimezone=UTC"
                + "&useSSL=false&allowPublicKeyRetrieval=true";
        return DriverManager.getConnection(url, user, pass);
    }

    /** Cria o banco (pela URL) e as tabelas, se ainda não existirem. */
    public static void inicializar() throws SQLException {
        String apartamento = """
                CREATE TABLE IF NOT EXISTS apartamento (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    numero VARCHAR(10) NOT NULL,
                    bloco VARCHAR(10) NOT NULL,
                    andar INT NOT NULL,
                    UNIQUE KEY uk_apartamento (numero, bloco)
                )""";
        String morador = """
                CREATE TABLE IF NOT EXISTS morador (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    cpf VARCHAR(11) NOT NULL UNIQUE,
                    telefone VARCHAR(15),
                    email VARCHAR(100),
                    cep VARCHAR(8),
                    logradouro VARCHAR(150),
                    bairro VARCHAR(100),
                    cidade VARCHAR(100),
                    uf CHAR(2),
                    apartamento_id INT NOT NULL,
                    CONSTRAINT fk_morador_apartamento FOREIGN KEY (apartamento_id) REFERENCES apartamento(id)
                )""";
        try (Connection c = getConexao(); Statement st = c.createStatement()) {
            st.execute(apartamento);
            st.execute(morador);
        }
    }
}
