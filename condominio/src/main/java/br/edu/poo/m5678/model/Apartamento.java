package br.edu.poo.m5678.model;

public class Apartamento {
    private int id;
    private String numero;
    private String bloco;
    private int andar;

    public Apartamento() {
    }

    public Apartamento(String numero, String bloco, int andar) {
        this.numero = numero;
        this.bloco = bloco;
        this.andar = andar;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getBloco() { return bloco; }
    public void setBloco(String bloco) { this.bloco = bloco; }
    public int getAndar() { return andar; }
    public void setAndar(int andar) { this.andar = andar; }

    @Override
    public String toString() {
        return String.format("ID: %d | Bloco: %s | Apto: %s | Andar: %d", id, bloco, numero, andar);
    }
}
