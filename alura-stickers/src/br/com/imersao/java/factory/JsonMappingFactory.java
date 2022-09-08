package br.com.imersao.java.factory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public abstract class JsonMappingFactory {

	private static final ObjectMapper MAPEADOR = new ObjectMapper();
	private static final JsonFactory FABRICA = new JsonFactory();

	public static ObjectReader gerarJsonReader() {

		return MAPEADOR.reader();

	}

	public static JsonFactory gerarJsonFactory() {

		return FABRICA;

	}

}
