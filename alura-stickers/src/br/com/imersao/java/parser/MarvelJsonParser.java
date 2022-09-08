package br.com.imersao.java.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMapperFactory;

public class MarvelJsonParser implements JsonParser {

	private final ObjectReader leitor;
	private Map<String, Map<String, String>> jsonMapeado;

	public MarvelJsonParser() {

		this.leitor = JsonMapperFactory.gerarJsonReader();

	}

	@Override
	public Map<String, Map<String, String>> parse(String json) {

		this.jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

		try {

			JsonNode arvoreJson = this.leitor.readTree(json).path("data").path("results");

			arvoreJson.forEach(folha -> this.jsonMapeado.put(folha.get("name").asText(),
					Map.ofEntries(Map.entry("descricao", folha.get("description").asText()),
							Map.entry("uriImagem", (folha.path("thumbnail").get("path").asText() + "."
									+ folha.path("thumbnail").get("extension").asText())))));

		} catch (JsonProcessingException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel processar o JSON informado.", e);

		}

		return this.jsonMapeado;

	}

}
