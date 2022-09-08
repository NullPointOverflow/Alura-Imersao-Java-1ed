package br.com.imersao.java.enumeration;

import java.awt.Color;

public enum Classificacao {

	PRIMEIRO(Color.decode("#FFD700")), SEGUNDO(Color.decode("#C0C0C0")), TERCEIRO(Color.decode("#8C7853")),
	OUTRAS_POSICOES(Color.WHITE);

	Color cor;

	private Classificacao(final Color cor) {

		this.cor = cor;

	}

	public Color getCor() {

		return this.cor;

	}

}
