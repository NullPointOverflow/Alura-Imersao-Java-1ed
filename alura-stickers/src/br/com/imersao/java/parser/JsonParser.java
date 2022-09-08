package br.com.imersao.java.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.factory.JsonMappingFactory;

public interface JsonParser {

	public Map<String, Map<String, String>> desserializar(String json);

	default String serializar(Map<String, Map<String, String>> conteudo) {

		String jsonConteudo;
		StringWriter escritor = new StringWriter();

		try (JsonGenerator geradorDeJson = JsonMappingFactory.gerarJsonFactory().createGenerator(escritor)) {

			if (conteudo.size() > 1) {

				geradorDeJson.writeStartArray();

			}

			for (Map.Entry<String, Map<String, String>> entrada : conteudo.entrySet()) {

				geradorDeJson.writeStartObject();

				geradorDeJson.writeStringField("titulo", entrada.getKey());

				geradorDeJson.writeStringField("descricao", entrada.getValue().get("descricao"));

				geradorDeJson.writeStringField("uriImagem", entrada.getValue().get("uriImagem"));

				geradorDeJson.writeNumberField("avaliacao", Integer.parseInt(entrada.getValue().get("avaliacao")));

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
