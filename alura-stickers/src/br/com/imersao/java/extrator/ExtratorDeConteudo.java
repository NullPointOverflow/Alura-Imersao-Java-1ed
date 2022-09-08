package br.com.imersao.java.extrator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.parser.JsonParser;
import br.com.imersao.java.util.Default;

public abstract class ExtratorDeConteudo {

	private JsonParser analisadorDeJSON;

	public ExtratorDeConteudo(JsonParser analisadorDeJSON) {

		this.analisadorDeJSON = analisadorDeJSON;

	}

	protected JsonParser getAnalisadorDeJSON() {

		return this.analisadorDeJSON;

	}

	public abstract List<Conteudo> extrair(String conteudoEstuturado);

	public String extrair(Conteudo entidadeConteudo) {

		Map<String, Map<String, String>> atributosLidos;
		String conteudoEstuturado;

		try {

			atributosLidos = new HashMap<String, Map<String, String>>(1);

			atributosLidos.put(entidadeConteudo.titulo(),
					Map.ofEntries(Map.entry("descricao", entidadeConteudo.descricao()),
							Map.entry("uriImagem", entidadeConteudo.uriImagem()),
							Map.entry("avaliacao", Integer.toString(entidadeConteudo.avaliacao()))));

			conteudoEstuturado = this.analisadorDeJSON.serializar(atributosLidos);

		} catch (ProcessamentoDeJSONException e) {

			System.out.println("AKAHFLKAHFJLFH");

			e.printStackTrace();

			return Default.getEmptyString();

		}

		return conteudoEstuturado;

	}

	public String extrair(List<Conteudo> entidadesConteudo) {

		Map<String, Map<String, String>> atributosLidos;
		String conteudosEstuturado;

		try {

			atributosLidos = new HashMap<String, Map<String, String>>(entidadesConteudo.size());

			entidadesConteudo.forEach(conteudo -> atributosLidos.put(conteudo.titulo(),
					Map.ofEntries(Map.entry("descricao", conteudo.descricao()),
							Map.entry("uriImagem", conteudo.uriImagem()),
							Map.entry("avaliacao", Integer.toString(conteudo.avaliacao())))));

			conteudosEstuturado = this.analisadorDeJSON.serializar(atributosLidos);

		} catch (ProcessamentoDeJSONException e) {

			return Default.getEmptyString();

		}

		return conteudosEstuturado;

	}

}
