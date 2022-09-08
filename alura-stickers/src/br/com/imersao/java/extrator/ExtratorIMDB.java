package br.com.imersao.java.extrator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.parser.IMDBJsonParser;
import br.com.imersao.java.util.Default;

public class ExtratorIMDB extends ExtratorDeConteudo {

	public ExtratorIMDB() {

		super(new IMDBJsonParser());

	}

	@Override
	public List<Conteudo> extrair(String conteudoEstuturado) {

		Map<String, Map<String, String>> atributosLidos;
		List<Conteudo> conteudos;

		try {

			atributosLidos = super.getAnalisadorDeJSON().desserializar(conteudoEstuturado);

			conteudos = new ArrayList<Conteudo>(atributosLidos.size());

			atributosLidos.forEach((k, v) -> {

				int avaliacao = new BigDecimal(v.get("avaliacao").replaceAll("^(?![\\s\\S])", "0"))
						.setScale(0, RoundingMode.HALF_EVEN).intValue();

				conteudos.add(new Conteudo(k.replaceAll("(\\/|:)", " -"), v.get("descricao"),
						v.get("uriImagem").replaceAll("(\\.\\_+)(.*).jpg$", "$1.jpg"), avaliacao));

			});

		} catch (ProcessamentoDeJSONException e) {

			return Default.getEmptyList();

		}

		return conteudos;

	}

}
