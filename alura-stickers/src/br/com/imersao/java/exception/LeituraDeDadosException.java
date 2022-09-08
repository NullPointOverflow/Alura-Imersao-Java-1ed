package br.com.imersao.java.exception;

public class LeituraDeDadosException extends RuntimeException {

	private static final long serialVersionUID = -1531770450702711250L;

	public LeituraDeDadosException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public LeituraDeDadosException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}
	
}
