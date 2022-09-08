package br.com.imersao.java.extrator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.parser.IMDBJsonParser;
import br.com.imersao.java.parser.JsonParser;
import br.com.imersao.java.record.Conteudo;
import br.com.imersao.java.util.Default;

public class ExtratorIMDB implements ExtratorDeConteudo {

	private JsonParser analisadorDeJSON;
	private Map<String, Map<String, String>> atributosLidos;
	private List<Conteudo> conteudos;

	public ExtratorIMDB() {

		this.analisadorDeJSON = new IMDBJsonParser();

	}

	@Override
	public List<Conteudo> extrair(String conteudoEstuturado) {

		try {

			this.atributosLidos = this.analisadorDeJSON.parse(conteudoEstuturado);

			this.conteudos = new ArrayList<Conteudo>(this.atributosLidos.size());

			this.atributosLidos.forEach((k, v) -> {

				int avaliacao = new BigDecimal(v.get("avaliacao").replaceAll("^(?![\\s\\S])", "0"))
						.setScale(0, RoundingMode.HALF_EVEN).intValue();

				this.conteudos.add(new Conteudo(k.replaceAll("(\\/|:)", " -"), v.get("descricao"),
						v.get("uriImagem").replaceAll("(\\.\\_+)(.*).jpg$", "$1.jpg"), avaliacao));

			});

		} catch (ProcessamentoDeJSONException e) {

			this.conteudos = Default.getEmptyList();

		}

		return conteudos;

	}

}
