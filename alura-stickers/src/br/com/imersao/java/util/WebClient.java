package br.com.imersao.java.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import br.com.imersao.java.exception.ConexaoWebException;

public class WebClient {

	private HttpClient clienteWeb;

	public WebClient() {

		this.clienteWeb = HttpClient.newHttpClient();

	}

	public String buscarDados(String url) {

		HttpRequest requisicao = null;
		HttpResponse<String> reposta = null;
		String conteudoDaResposta = "";

		try {

			URI endereco = URI.create(url);

			requisicao = HttpRequest.newBuilder().uri(endereco).GET().build();

			reposta = this.clienteWeb.send(requisicao, BodyHandlers.ofString(StandardCharsets.UTF_8));

			conteudoDaResposta = reposta.body();

		} catch (IOException | InterruptedException e) {

			throw new ConexaoWebException("Problema com na conexão estabelecida com a URL informada.", e);

		}

		return conteudoDaResposta;

	}

}
