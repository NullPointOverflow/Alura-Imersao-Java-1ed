package br.com.imersao.java.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMappingFactory;

public class NASAJsonParser implements JsonParser {

	private ObjectReader leitor;

	public NASAJsonParser() {

		this.leitor = JsonMappingFactory.gerarJsonReader();

	}

	@Override
	public Map<String, Map<String, String>> desserializar(String json) {

		JsonNode arvoreJson;
		Map<String, Map<String, String>> jsonMapeado;

		try {

			arvoreJson = this.leitor.readTree(json);

			jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

			jsonMapeado.put(arvoreJson.get("title").asText(),
					Map.ofEntries(Map.entry("descricao", arvoreJson.get("explanation").asText()),
							Map.entry("uriImagem", arvoreJson.get("url").asText())));

		} catch (JsonProcessingException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel processar o JSON informado.", e);

		}

		return jsonMapeado;

	}

}
