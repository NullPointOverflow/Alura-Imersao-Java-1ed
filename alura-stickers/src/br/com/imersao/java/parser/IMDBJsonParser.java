package br.com.imersao.java.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMapperFactory;

public class IMDBJsonParser implements JsonParser {

	private final ObjectReader leitor;
	private Map<String, Map<String, String>> jsonMapeado;

	public IMDBJsonParser() {

		this.leitor = JsonMapperFactory.gerarJsonReader();

	}

	private void parseLista(JsonNode arvoreJson) {

		this.jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

		arvoreJson.forEach(folha -> jsonMapeado.put(folha.get("fullTitle").asText(),
				Map.ofEntries(Map.entry("descricao", folha.get("year").asText()),
						Map.entry("uriImagem", folha.get("image").asText()),
						Map.entry("avaliacao", folha.get("imDbRating").asText()))));

	}

	private void parseResultadoDeBusca(JsonNode arvoreJson) {

		this.jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

		arvoreJson.forEach(folha -> jsonMapeado.put(folha.get("title").asText(),
				Map.ofEntries(Map.entry("descricao", folha.get("description").asText()),
						Map.entry("uriImagem", folha.get("image").asText()), Map.entry("avaliacao", "0"))));

	}

	@Override
	public Map<String, Map<String, String>> parse(String json) {

		try {

			JsonNode arvoreJson = this.leitor.readTree(json);

			if (!(arvoreJson.path("items").isMissingNode())) {

				this.parseLista(arvoreJson.path("items"));

			} else {

				this.parseResultadoDeBusca(arvoreJson.path("results"));

			}

		} catch (JsonProcessingException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel processar o JSON informado.", e);

		}

		return this.jsonMapeado;

	}

}
