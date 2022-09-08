
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

	private static final ObjectMapper MAPEADOR = new ObjectMapper();

	public List<Map<String, String>> parseListagem(String json) {

		List<Map<String, String>> jsonProcessado = Collections.emptyList();

		try {

			JsonNode arvoreJson = MAPEADOR.readTree(json).get("items");

			if (arvoreJson.isEmpty()) {

				return jsonProcessado;

			}

			List<Map<String, String>> jsonMapeado = MAPEADOR.convertValue(arvoreJson,
					new TypeReference<List<Map<String, String>>>() {
					});

			jsonProcessado = new ArrayList<Map<String, String>>(jsonMapeado.size());

			for (Map<String, String> no : jsonMapeado) {

				jsonProcessado.add(Map.of("titulo", no.get("fullTitle"), "urlImagem",
						no.get("image").replaceAll("(\\.\\_+)(.*).jpg$", "$1.jpg"), "avaliacao",
						no.get("imDbRating")));

			}

		} catch (JsonProcessingException e) {

			throw new RuntimeException("Não foi possivel processar o JSON informado.", e);

		}

		return jsonProcessado;

	}

	public List<Map<String, String>> parseResultadoDeBusca(String json) {

		List<Map<String, String>> jsonProcessado = Collections.emptyList();

		try {

			JsonNode arvoreJson = MAPEADOR.readTree(json).get("results");

			if (arvoreJson.isEmpty()) {

				return jsonProcessado;

			}

			List<Map<String, String>> jsonMapeado = MAPEADOR.convertValue(arvoreJson,
					new TypeReference<List<Map<String, String>>>() {
					});

			jsonProcessado = new ArrayList<Map<String, String>>(jsonMapeado.size());

			for (Map<String, String> no : jsonMapeado) {

				jsonProcessado.add(Map.of("titulo", no.get("title"), "descricao", no.get("description"), "urlImagem",
						no.get("image").replaceAll("(\\.\\_+)(.*).jpg$", "$1.jpg")));

			}

		} catch (JsonProcessingException e) {

			throw new RuntimeException("Não foi possivel processar o JSON informado.", e);

		}

		return jsonProcessado;

	}

}
