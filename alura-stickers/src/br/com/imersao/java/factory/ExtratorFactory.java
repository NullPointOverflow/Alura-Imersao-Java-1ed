package br.com.imersao.java.factory;

import br.com.imersao.java.exception.CriacaoDeExtratorDeConteudoException;
import br.com.imersao.java.extrator.ExtratorDeConteudo;

public abstract class ExtratorFactory {

	public static ExtratorDeConteudo gerar(String extratorEsperado) {

		Class<?> pseudoExtrator;
		ExtratorDeConteudo extrator;

		try {

			pseudoExtrator = Class.forName("br.com.imersao.java.extrator." + extratorEsperado);

			extrator = (ExtratorDeConteudo) pseudoExtrator.getDeclaredConstructor().newInstance();

		} catch (ClassNotFoundException e) {

			throw new CriacaoDeExtratorDeConteudoException(
					"O extrator de conteúdo solicitado não foi localizado no sistema.", e);

		} catch (ReflectiveOperationException | RuntimeException e) {

			throw new CriacaoDeExtratorDeConteudoException("Não foi possivel gerar o estrator de conteúdo solicitado.",
					e);

		}

		return extrator;

	}

}
