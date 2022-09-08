package br.com.imersao.java.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMappingFactory;

public class MarvelJsonParser implements JsonParser {

	private ObjectReader leitor;

	public MarvelJsonParser() {

		this.leitor = JsonMappingFactory.gerarJsonReader();

	}

	@Override
	public Map<String, Map<String, String>> desserializar(String json) {

		JsonNode arvoreJson;
		Map<String, Map<String, String>> jsonMapeado;

		try {

			arvoreJson = this.leitor.readTree(json).path("data").path("results");

			jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

			arvoreJson.forEach(folha -> jsonMapeado.put(folha.get("name").asText(),
					Map.ofEntries(Map.entry("descricao", folha.get("description").asText()),
							Map.entry("uriImagem", (folha.path("thumbnail").get("path").asText() + "."
									+ folha.path("thumbnail").get("extension").asText())))));

		} catch (JsonProcessingException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel processar o JSON informado.", e);

		}

		return jsonMapeado;

	}

}
