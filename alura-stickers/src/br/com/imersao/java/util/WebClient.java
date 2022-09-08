package br.com.imersao.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import br.com.imersao.java.exception.ConexaoWebException;

public class WebClient {

	private HttpClient clienteWeb;
	private URI endereco;
	private HttpRequest requisicao;
	private HttpResponse<String> reposta;

	public WebClient() {

		this.clienteWeb = HttpClient.newHttpClient();

	}

	public String buscarDados(String url) {

		String conteudoDaResposta = "";

		try {

			this.endereco = URI.create(url);

			this.requisicao = HttpRequest.newBuilder().uri(this.endereco).GET().build();

			this.reposta = this.clienteWeb.send(this.requisicao,
					HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

			conteudoDaResposta = this.reposta.body();

		} catch (IOException | InterruptedException e) {

			throw new ConexaoWebException("Problema na conexão estabelecida com a URL informada.", e);

		}

		return conteudoDaResposta;

	}

	public int enviarDados(String url, String conteudoJson) {

		int statusDaResposta = 0;

		try {

			this.endereco = URI.create(url);

			this.requisicao = HttpRequest.newBuilder().uri(this.endereco).header("Content-Type", "application/json")
					.method("PATCH", HttpRequest.BodyPublishers.ofString(conteudoJson)).build();

			this.reposta = this.clienteWeb.send(this.requisicao,
					HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

			statusDaResposta = this.reposta.statusCode();

		} catch (IOException | InterruptedException e) {

			throw new ConexaoWebException("Problema na conexão estabelecida com a URL informada.", e);

		}

		return statusDaResposta;

	}

	public InputStream abrirFluxoDeDados(String link) {

		InputStream fluxoDeDados;

		try {

			fluxoDeDados = new URL(link).openStream();

			return fluxoDeDados;

		} catch (IOException e) {

			throw new ConexaoWebException("Não foi possivel estabelecer uma conexao com a URL informada.", e);

		}

	}

}
