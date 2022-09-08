package br.com.imersao.java.enumeration;

import java.awt.Color;

public enum Avaliacao {

	ESPETACULAR("ESPETACULAR", Color.BLUE), BOM("BOM", Color.GREEN), MEDIANO("MEDIANO", Color.YELLOW),
	RUIM("RUIM", Color.RED);

	String textual;
	Color cor;

	private Avaliacao(final String textual, final Color cor) {

		this.textual = textual;

		this.cor = cor;

	}

	public String getString() {

		return this.textual;

	}

	public Color getCor() {

		return this.cor;

	}

}
