package br.com.imersao.java.factory.sticker;

import java.util.List;
import java.util.Map;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.util.WebClient;

public abstract class StickerFactory {

	private static final String PASTA_DE_FIGURINHAS = "sticker/";
	private WebClient clienteHTTP;

	public StickerFactory() {

		this.clienteHTTP = new WebClient();

	}

	protected static String getDiretorioDeDestino() {

		return PASTA_DE_FIGURINHAS;

	}

	protected WebClient getClienteHTTP() {

		return this.clienteHTTP;

	}

	public abstract Map<String, Integer> gerar(List<Conteudo> listaDeConteudo);

}
