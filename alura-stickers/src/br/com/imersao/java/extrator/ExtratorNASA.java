package br.com.imersao.java.extrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.parser.NASAJsonParser;
import br.com.imersao.java.util.Default;

public class ExtratorNASA extends ExtratorDeConteudo {

	public ExtratorNASA() {

		super(new NASAJsonParser());

	}

	@Override
	public List<Conteudo> extrair(String conteudoEstuturado) {

		Map<String, Map<String, String>> atributosLidos;
		List<Conteudo> conteudos;

		try {

			atributosLidos = super.getAnalisadorDeJSON().desserializar(conteudoEstuturado);

			conteudos = new ArrayList<Conteudo>(atributosLidos.size());

			atributosLidos.forEach((k, v) -> conteudos
					.add(new Conteudo(k.replaceAll("(\\/|:)", " -"), v.get("descricao"), v.get("uriImagem"))));

		} catch (ProcessamentoDeJSONException e) {

			return Default.getEmptyList();

		}

		return conteudos;

	}

}
