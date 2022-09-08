package br.com.imersao.java.exception;

public class ArquivoDeConfiguracaoException extends RuntimeException {

	private static final long serialVersionUID = 6606726401577121801L;

	public ArquivoDeConfiguracaoException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public ArquivoDeConfiguracaoException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}

}
