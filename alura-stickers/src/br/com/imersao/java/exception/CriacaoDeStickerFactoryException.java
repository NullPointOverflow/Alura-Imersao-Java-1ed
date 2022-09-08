package br.com.imersao.java.exception;

public class CriacaoDeStickerFactoryException extends RuntimeException {

	private static final long serialVersionUID = 6501139390400401036L;

	public CriacaoDeStickerFactoryException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public CriacaoDeStickerFactoryException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}

}
