
public enum SimboloUnicode {

	ACENO("\uD83D\uDC4B"), AVISO("\u26A0\uFE0F"), ESTRELA("\u2B50"), ESTRELA_BRILHANTE("\uD83C\uDF1F"),
	POSITIVO("\uD83D\uDC4D"), PROIBIDO("\uD83D\uDEAB"), ROSTO("\uD83D\uDE42");

	private String simbolo;

	private SimboloUnicode(String simbolo) {

		this.simbolo = simbolo;

	}

	public String getSimbolo() {

		return this.simbolo;

	}

}
