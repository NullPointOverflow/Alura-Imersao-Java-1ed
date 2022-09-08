package br.com.imersao.java.exception;

public class ConexaoWebException extends RuntimeException {

	private static final long serialVersionUID = 4126597940666948389L;

	public ConexaoWebException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public ConexaoWebException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}

}
