CREATE DATABASE IF NOT EXISTS condominio;
USE condominio;

CREATE TABLE IF NOT EXISTS apartamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL,
    bloco VARCHAR(10) NOT NULL,
    andar INT NOT NULL,
    UNIQUE KEY uk_apartamento (numero, bloco)
);

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
);
