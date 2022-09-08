package br.com.imersao.java.enumeration;

public enum SimboloUnicode {

	ACENO("\uD83D\uDC4B"), APROVADO("\u2705"), ATUALIZACAO("\uD83D\uDD03"), AVISO("\u26A0\uFE0F"),
	CONSTRUCAO("\uD83D\uDEA7"), BANDEIRA_LARGADA("\uD83C\uDFC1"), ESTRELA("\u2B50"), ESTRELA_BRILHANTE("\uD83C\uDF1F"),
	NEGATIVO("\uD83D\uDC4E"), PARADA("\u270B"), POSITIVO("\uD83D\uDC4D"), PROIBIDO("\uD83D\uDEAB"), REPROVADO("\u274C"),
	ROSTO("\uD83D\uDE42");

	private String simbolo;

	private SimboloUnicode(final String simbolo) {

		this.simbolo = simbolo;

	}

	public String getSimbolo() {

		return this.simbolo;

	}

}
