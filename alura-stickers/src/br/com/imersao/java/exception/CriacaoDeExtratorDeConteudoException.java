package br.com.imersao.java.exception;

public class CriacaoDeExtratorDeConteudoException extends RuntimeException {

	private static final long serialVersionUID = -399210827994401739L;

	public CriacaoDeExtratorDeConteudoException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public CriacaoDeExtratorDeConteudoException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}

}
