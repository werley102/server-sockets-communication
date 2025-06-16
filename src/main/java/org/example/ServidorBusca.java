package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;

public abstract class ServidorBusca {

    public ServidorBusca(int porta, String nomeServidor, String arquivoDados) {
    }

    public void iniciar() {

    }

    private void carregarDados() throws IOException {

    }

    private void processarRequisicao(Socket socket) {

    }

    private List<JSONObject> buscar(String query) {

    }

    public void parar() {

    }
}
package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public abstract class ServidorBusca {
    private final int porta;
    private final String nomeServidor;
    private final String arquivoDados;
    private ServerSocket serverSocket;
    private List<JSONObject> dados;
    private AlgoritmoBusca algoritmoBusca;

    public ServidorBusca(int porta, String nomeServidor, String arquivoDados) {
        this.porta = porta;
        this.nomeServidor = nomeServidor;
        this.arquivoDados = arquivoDados;
        this.algoritmoBusca = new BoyerMoore(); // Usando Boyer-Moore como algoritmo padrão
    }

    public void iniciar() {
        try {
            // Carrega os dados do arquivo JSON
            carregarDados();
            
            System.out.println(nomeServidor + " iniciado na porta " + porta);
            System.out.println("Dados carregados: " + dados.size() + " registros");
            
        } catch (IOException e) {
            System.err.println("Erro ao iniciar " + nomeServidor + ": " + e.getMessage());
        }
    }

    private void carregarDados() throws IOException {
        try {
            String conteudo = Files.readString(Paths.get(arquivoDados));
            JSONArray jsonArray = new JSONArray(conteudo);

            dados = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                dados.add(jsonArray.getJSONObject(i));
            }

            System.out.println(nomeServidor + " - Carregados " + dados.size() + " artigos");
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo de dados: " + e.getMessage());
            throw e;
        }
    }

    private List<JSONObject> buscar(String query) {
        List<JSONObject> resultados = new ArrayList<>();
        String queryLower = query.toLowerCase();

        long startTime = System.currentTimeMillis();

        for (JSONObject artigo : dados) {
            String titulo = artigo.optString("title", "").toLowerCase();
            String resumo = artigo.optString("abstract", "").toLowerCase();

            // Busca usando o algoritmo Boyer-Moore
            if (algoritmoBusca.buscar(titulo, queryLower) != -1 ||
                    algoritmoBusca.buscar(resumo, queryLower) != -1) {

                // Cria objeto resultado com informações relevantes
                JSONObject resultado = new JSONObject();
                resultado.put("title", artigo.optString("title", ""));
                resultado.put("abstract", artigo.optString("abstract", "").substring(0,
                        Math.min(200, artigo.optString("abstract", "").length())) + "...");
                resultado.put("label", artigo.optString("label", ""));
                resultado.put("servidor", nomeServidor);

                resultados.add(resultado);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println(nomeServidor + " - Busca por '" + query + "' concluída em " + 
                          (endTime - startTime) + "ms. " + resultados.size() + " resultados encontrados");

        return resultados;
    }

    public void parar() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao parar servidor: " + e.getMessage());
        }
    }
}
