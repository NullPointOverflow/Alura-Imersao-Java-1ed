package br.com.imersao.java.factory;

import br.com.imersao.java.apresentador.ApresentadorDeConteudo;
import br.com.imersao.java.exception.CriacaoDeExtratorDeConteudoException;
import br.com.imersao.java.ui.componente.InterfaceDeTexto;

public abstract class ApresentadorFactory {

	public static ApresentadorDeConteudo gerar(String apresentadorEsperado, InterfaceDeTexto controleDeTexto) {

		Class<?> pseudoApresentador;
		ApresentadorDeConteudo apresentador;

		try {

			pseudoApresentador = Class.forName("br.com.imersao.java.apresentador." + apresentadorEsperado);

			apresentador = (ApresentadorDeConteudo) pseudoApresentador
					.getDeclaredConstructor(controleDeTexto.getClass()).newInstance(controleDeTexto);

		} catch (ClassNotFoundException e) {

			throw new CriacaoDeExtratorDeConteudoException(
					"O apresentador de conteúdo solicitado não foi localizado no sistema.", e);

		} catch (ReflectiveOperationException | RuntimeException e) {

			throw new CriacaoDeExtratorDeConteudoException(
					"Não foi possivel gerar o apresentador de conteúdo solicitado.", e);

		}

		return apresentador;

	}

}
