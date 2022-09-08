package br.com.imersao.java.extrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.parser.JsonParser;
import br.com.imersao.java.parser.NASAJsonParser;
import br.com.imersao.java.record.Conteudo;
import br.com.imersao.java.util.Default;

public class ExtratorNASA implements ExtratorDeConteudo {

	private JsonParser analisadorDeJSON;
	private Map<String, Map<String, String>> atributosLidos;
	private List<Conteudo> conteudos;

	public ExtratorNASA() {

		this.analisadorDeJSON = new NASAJsonParser();

	}

	@Override
	public List<Conteudo> extrair(String conteudoEstuturado) {

		try {

			this.atributosLidos = this.analisadorDeJSON.parse(conteudoEstuturado);

			this.conteudos = new ArrayList<Conteudo>(this.atributosLidos.size());

			this.atributosLidos.forEach((k, v) -> this.conteudos
					.add(new Conteudo(k.replaceAll("(\\/|:)", " -"), v.get("descricao"), v.get("uriImagem"))));

		} catch (ProcessamentoDeJSONException e) {

			this.conteudos = Default.getEmptyList();

		}

		return this.conteudos;

	}

}
