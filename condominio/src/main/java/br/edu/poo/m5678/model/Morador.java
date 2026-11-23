package br.edu.poo.m5678.model;

public class Morador {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private int apartamentoId;
    private String apartamentoDescricao; // preenchido pelo JOIN

    public Morador() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    public int getApartamentoId() { return apartamentoId; }
    public void setApartamentoId(int apartamentoId) { this.apartamentoId = apartamentoId; }
    public String getApartamentoDescricao() { return apartamentoDescricao; }
    public void setApartamentoDescricao(String apartamentoDescricao) { this.apartamentoDescricao = apartamentoDescricao; }

    private static String vazioSeNulo(String s) {
        return s == null || s.isBlank() ? "-" : s;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | CPF: %s | Tel: %s | Email: %s%n"
                        + "      Endereço: %s, %s - %s/%s (CEP %s)%n"
                        + "      Apartamento: %s",
                id, nome, cpf, vazioSeNulo(telefone), vazioSeNulo(email),
                vazioSeNulo(logradouro), vazioSeNulo(bairro), vazioSeNulo(cidade), vazioSeNulo(uf), vazioSeNulo(cep),
                apartamentoDescricao != null ? apartamentoDescricao : "ID " + apartamentoId);
    }
}
