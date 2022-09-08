package br.com.imersao.java.ui.componente;

public class InterfaceDeTexto {

	public void exibirQuebraDeLinha() {

		System.out.print(System.lineSeparator());

	}

	public void exibirQuebraDuplaDeLinha() {

		System.out.print(System.lineSeparator() + System.lineSeparator());

	}

	public void exibirQuebraTriplaDeLinha() {

		System.out.print(System.lineSeparator() + System.lineSeparator() + System.lineSeparator());

	}

	public void exibirTexto(String mensagem) {

		System.out.print(mensagem);

	}

}
