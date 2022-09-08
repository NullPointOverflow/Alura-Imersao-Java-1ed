package br.com.imersao.java.exception;

public class GeracaoDeHashException extends RuntimeException {

	private static final long serialVersionUID = 6606726401577121801L;

	public GeracaoDeHashException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public GeracaoDeHashException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}

}
