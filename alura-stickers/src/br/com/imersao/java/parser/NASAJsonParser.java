package br.com.imersao.java.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMapperFactory;

public class NASAJsonParser implements JsonParser {

	private final ObjectReader leitor;
	private Map<String, Map<String, String>> jsonMapeado;

	public NASAJsonParser() {

		this.leitor = JsonMapperFactory.gerarJsonReader();

	}

	@Override
	public Map<String, Map<String, String>> parse(String json) {

		this.jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

		try {

			JsonNode arvoreJson = this.leitor.readTree(json);

			this.jsonMapeado.put(arvoreJson.get("title").asText(),
					Map.ofEntries(Map.entry("descricao", arvoreJson.get("explanation").asText()),
							Map.entry("uriImagem", arvoreJson.get("url").asText())));

		} catch (JsonProcessingException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel processar o JSON informado.", e);

		}

		return this.jsonMapeado;

	}

}
