package br.com.imersao.java.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMappingFactory;

public class LinguagemDeProgramacaoJsonParser implements JsonParser {

	private ObjectReader leitor;

	public LinguagemDeProgramacaoJsonParser() {

		this.leitor = JsonMappingFactory.gerarJsonReader();

	}

	@Override
	public Map<String, Map<String, String>> desserializar(String json) {

		JsonNode arvoreJson;
		Map<String, Map<String, String>> jsonMapeado;

		try {

			arvoreJson = this.leitor.readTree(json);

			jsonMapeado = new LinkedHashMap<String, Map<String, String>>();

			arvoreJson.forEach(folha -> jsonMapeado.put(folha.get("nome").asText(),
					Map.ofEntries(Map.entry("descricao", folha.get("descricao").asText()),
							Map.entry("logotipo", folha.get("logotipo").asText()),
							Map.entry("utilizadores", folha.get("utilizadores").asText()))));

		} catch (JsonProcessingException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel processar o JSON informado.", e);

		}

		return jsonMapeado;

	}

	@Override
	public String serializar(Map<String, Map<String, String>> conteudo) {

		String jsonConteudo;
		StringWriter escritor = new StringWriter();

		try (JsonGenerator geradorDeJson = JsonMappingFactory.gerarJsonFactory().createGenerator(escritor)) {

			if (conteudo.size() > 1) {

				geradorDeJson.writeStartArray();

			}

			for (Map.Entry<String, Map<String, String>> entrada : conteudo.entrySet()) {

				geradorDeJson.writeStartObject();

				geradorDeJson.writeStringField("nome", entrada.getKey());

				geradorDeJson.writeStringField("descricao", entrada.getValue().get("descricao"));

				geradorDeJson.writeStringField("logotipo", entrada.getValue().get("logotipo"));

				geradorDeJson.writeNumberField("utilizadores",
						Integer.parseInt(entrada.getValue().get("utilizadores")));

				geradorDeJson.writeEndObject();

			}

			if (conteudo.size() > 1) {

				geradorDeJson.writeEndArray();

			}

			geradorDeJson.close();

			jsonConteudo = escritor.toString();

		} catch (IOException e) {

			throw new ProcessamentoDeJSONException("Não foi possivel gerar o JSON solicitado.", e);

		}

		return jsonConteudo;

	}

}
