
import java.awt.Color;

public enum Classificacao {

	ESPETACULAR("ESPETACULAR", Color.BLUE), BOM("BOM", Color.GREEN), MEDIANO("MEDIANO", Color.YELLOW),
	RUIM("RUIM", Color.RED), NAO_AVALIADO("NÃO AVALIADO", Color.WHITE);

	String textual;
	Color cor;

	private Classificacao(final String textual, final Color cor) {

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
