package br.edu.poo.m5678.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/** Consome a API pública ViaCEP (https://viacep.com.br). */
public class ViaCepService {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private ViaCepService() {
    }

    /**
     * Busca o endereço de um CEP.
     *
     * @return JSON com logradouro, bairro, localidade e uf; ou null se o CEP não existir.
     * @throws Exception se não houver internet ou a API falhar.
     */
    public static JsonObject buscar(String cep) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://viacep.com.br/ws/" + cep + "/json/"))
                .timeout(Duration.ofSeconds(8))
                .GET()
                .build();
        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 400) {
            return null;
        }
        if (resp.statusCode() != 200) {
            throw new Exception("ViaCEP respondeu com status " + resp.statusCode());
        }
        JsonObject json = JsonParser.parseString(resp.body()).getAsJsonObject();
        return json.has("erro") ? null : json;
    }
}
