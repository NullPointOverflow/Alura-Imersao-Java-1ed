package br.com.imersao.java.parser;

import java.util.Map;

public interface JsonParser {

	public Map<String, Map<String, String>> parse(String json);

}
