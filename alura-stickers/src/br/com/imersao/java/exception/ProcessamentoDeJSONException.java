package br.com.imersao.java.exception;

public class ProcessamentoDeJSONException extends RuntimeException {

	private static final long serialVersionUID = 8369300670025093426L;

	public ProcessamentoDeJSONException(String mensagemDeErro) {

		super(mensagemDeErro);

	}

	public ProcessamentoDeJSONException(String mensagemDeErro, Exception excecao) {

		super(mensagemDeErro, excecao);

	}

}
