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

    public ServidorBusca(int porta, String nomeServidor, String arquivoDados) {
        this.porta = porta;
        this.nomeServidor = nomeServidor;
        this.arquivoDados = arquivoDados;
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
