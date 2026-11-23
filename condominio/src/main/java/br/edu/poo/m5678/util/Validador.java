package br.edu.poo.m5678.util;

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern EMAIL = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private Validador() {
    }

    public static boolean vazio(String s) {
        return s == null || s.isBlank();
    }

    public static String somenteDigitos(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }

    public static boolean emailValido(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean cepValido(String cep) {
        return somenteDigitos(cep).length() == 8;
    }

    public static boolean telefoneValido(String tel) {
        int n = somenteDigitos(tel).length();
        return n == 10 || n == 11;
    }

    /** Valida CPF com os dígitos verificadores. */
    public static boolean cpfValido(String cpf) {
        String d = somenteDigitos(cpf);
        if (d.length() != 11 || d.chars().distinct().count() == 1) {
            return false;
        }
        return digitoVerificador(d, 9) == d.charAt(9) - '0'
                && digitoVerificador(d, 10) == d.charAt(10) - '0';
    }

    private static int digitoVerificador(String d, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (d.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
