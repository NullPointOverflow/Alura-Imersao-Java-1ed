package br.com.imersao.java.exception;

import java.io.IOException;

public class GeracaoDeStickerException extends IOException {

	private static final long serialVersionUID = 3530689715068553766L;

	public GeracaoDeStickerException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public GeracaoDeStickerException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}

}
