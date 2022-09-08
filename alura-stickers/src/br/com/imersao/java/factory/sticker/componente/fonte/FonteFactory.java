package br.com.imersao.java.factory.sticker.componente.fonte;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class FonteFactory {

	private static final Font FONTE_PADRAO = new Font(Font.MONOSPACED, Font.BOLD, 1);
	private static Font FONTE_SELECIONADA;
	private double proporcaoFonteCabecalho;
	private double proporcaoFonteCentro;
	private double proporcaoFonteRodape;

	public FonteFactory() {

		try (InputStream pseudoFonte = FonteFactory.class.getResourceAsStream("/fonte/impact.ttf")) {

			FONTE_SELECIONADA = Font.createFont(Font.TRUETYPE_FONT, pseudoFonte);

		} catch (IOException | FontFormatException e) {

			FONTE_SELECIONADA = FONTE_PADRAO;

		}

	}

	public static final Font getFontePadrao() {

		return FONTE_PADRAO;

	}

	public static final Font getFonteSelecionada() {

		return FONTE_SELECIONADA;

	}

	public double getProporcaoFonteDeCabecalho() {

		return this.proporcaoFonteCabecalho;

	}

	public void setProporcaoFonteDeCabecalho(double proporcaoFonteCabecalho) {

		this.proporcaoFonteCabecalho = proporcaoFonteCabecalho;

	}

	public double getProporcaoFonteDeCentro() {

		return this.proporcaoFonteCentro;

	}

	public void setProporcaoFonteDeCentro(double proporcaoFonteCentro) {

		this.proporcaoFonteCentro = proporcaoFonteCentro;

	}

	public double getProporcaoFonteDeRodape() {

		return this.proporcaoFonteRodape;

	}

	public void setProporcaoFonteDeRodape(double proporcaoFonteRodape) {

		this.proporcaoFonteRodape = proporcaoFonteRodape;

	}

//	PUBLIC FONT GERARFONTEDECABECALHO(BUFFEREDIMAGE IMAGEM, DOUBLE PROPORCAO) {
//
//		FONT FONTECABECALHO = FONTE_SELECIONADA.DERIVEFONT((FLOAT) (IMAGEM.GETHEIGHT() * PROPORCAO));
//		
//		//FONT FONTECABECALHO = FONTE_SELECIONADA.DERIVEFONT((FLOAT) (IMAGEM.GETHEIGHT() * 0.06));
//
//		RETURN FONTECABECALHO;
//
//	}

	public abstract Font gerarFonteDeCabecalho(BufferedImage imagem);

	public abstract Font gerarFonteDeCentro(BufferedImage imagem);

	public abstract Font gerarFonteDeRodape(BufferedImage imagem);

}
