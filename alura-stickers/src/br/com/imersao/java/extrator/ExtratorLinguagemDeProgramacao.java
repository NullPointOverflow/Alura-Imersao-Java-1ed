package br.com.imersao.java.extrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.exception.ProcessamentoDeJSONException;
import br.com.imersao.java.parser.LinguagemDeProgramacaoJsonParser;
import br.com.imersao.java.util.Default;

public class ExtratorLinguagemDeProgramacao extends ExtratorDeConteudo {

	public ExtratorLinguagemDeProgramacao() {

		super(new LinguagemDeProgramacaoJsonParser());

	}

	@Override
	public List<Conteudo> extrair(String conteudoEstuturado) {

		Map<String, Map<String, String>> atributosLidos;
		List<Conteudo> conteudos;

		try {

			atributosLidos = super.getAnalisadorDeJSON().desserializar(conteudoEstuturado);

			conteudos = new ArrayList<Conteudo>(atributosLidos.size());

			atributosLidos.forEach((k, v) -> conteudos.add(
					new Conteudo(k, v.get("descricao"), v.get("logotipo"), Integer.parseInt(v.get("utilizadores")))));

		} catch (ProcessamentoDeJSONException e) {

			return Default.getEmptyList();

		}

		return conteudos;

	}

	@Override
	public String extrair(Conteudo entidadeConteudo) {

		Map<String, Map<String, String>> atributosLidos;
		String conteudoEstuturado;

		try {

			atributosLidos = new HashMap<String, Map<String, String>>(1);

			atributosLidos.put(entidadeConteudo.titulo(),
					Map.ofEntries(Map.entry("descricao", entidadeConteudo.descricao()),
							Map.entry("logotipo", entidadeConteudo.uriImagem()),
							Map.entry("utilizadores", Integer.toString(entidadeConteudo.avaliacao()))));

			conteudoEstuturado = super.getAnalisadorDeJSON().serializar(atributosLidos);

		} catch (ProcessamentoDeJSONException e) {

			return Default.getEmptyString();

		}

		return conteudoEstuturado;

	}

	@Override
	public String extrair(List<Conteudo> entidadesConteudo) {

		Map<String, Map<String, String>> atributosLidos;
		String conteudosEstuturado;

		try {

			atributosLidos = new HashMap<String, Map<String, String>>(entidadesConteudo.size());

			entidadesConteudo.forEach(conteudo -> atributosLidos.put(conteudo.titulo(),
					Map.ofEntries(Map.entry("descricao", conteudo.descricao()),
							Map.entry("logotipo", conteudo.uriImagem()),
							Map.entry("utilizadores", Integer.toString(conteudo.avaliacao())))));

			conteudosEstuturado = super.getAnalisadorDeJSON().serializar(atributosLidos);

		} catch (ProcessamentoDeJSONException e) {

			return Default.getEmptyString();

		}

		return conteudosEstuturado;

	}

}
