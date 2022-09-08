package br.com.imersao.java.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public abstract class JsonMapperFactory {

	private static final ObjectMapper MAPEADOR = new ObjectMapper();

	public static ObjectReader gerarJsonReader() {

		return MAPEADOR.reader();

	}

	public static ObjectWriter gerarJsonWriter() {

		return MAPEADOR.writer();

	}

}
