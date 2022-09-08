package br.com.imersao.java.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMappingFactory;

public class IMDBJsonParser implements JsonParser {

	private ObjectReader leitor;

	public IMDBJsonParser() {

		this.leitor = JsonMappingFactory.gerarJsonReader();

	}

	private Map<String, Map<String, String>> desserializarListagem(JsonNode arvoreJson) {

		Map<String, Map<String, String>> jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

		arvoreJson.forEach(folha -> jsonMapeado.put(folha.get("fullTitle").asText(),
				Map.ofEntries(Map.entry("descricao", folha.get("year").asText()),
						Map.entry("uriImagem", folha.get("image").asText()),
						Map.entry("avaliacao", folha.get("imDbRating").asText()))));

		return jsonMapeado;

	}

	private Map<String, Map<String, String>> desserializarResultadoDeBusca(JsonNode arvoreJson) {

		Map<String, Map<String, String>> jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

		arvoreJson.forEach(folha -> jsonMapeado.put(folha.get("title").asText(),
				Map.ofEntries(Map.entry("descricao", folha.get("description").asText()),
						Map.entry("uriImagem", folha.get("image").asText()), Map.entry("avaliacao", "0"))));

		return jsonMapeado;

	}

	@Override
	public Map<String, Map<String, String>> desserializar(String json) {

		JsonNode arvoreJson;
		Map<String, Map<String, String>> jsonMapeado;

		try {

			arvoreJson = this.leitor.readTree(json);

			if (!(arvoreJson.path("items").isMissingNode())) {

				jsonMapeado = this.desserializarListagem(arvoreJson.path("items"));

			} else {

				jsonMapeado = this.desserializarResultadoDeBusca(arvoreJson.path("results"));

			}

		} catch (JsonProcessingException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel processar o JSON informado.", e);

		}

		return jsonMapeado;

	}

}
