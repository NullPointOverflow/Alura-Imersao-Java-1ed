package br.com.imersao.java.factory.sticker.componente.fonte;

import java.awt.Font;
import java.awt.image.BufferedImage;

public class ImagemVerticalFonteFactory extends FonteFactory {

	@Override
	public Font gerarFonteDeCabecalho(BufferedImage imagem) {

		Font fonteCabecalho = super.getFonteSelecionada()
				.deriveFont((float) (imagem.getWidth() * super.getProporcaoFonteDeCabecalho()));

		return fonteCabecalho;

	}

	@Override
	public Font gerarFonteDeCentro(BufferedImage imagem) {

		Font fonteCentral = super.getFontePadrao()
				.deriveFont((float) (imagem.getWidth() * super.getProporcaoFonteDeCentro()));

		return fonteCentral;

	}

	@Override
	public Font gerarFonteDeRodape(BufferedImage imagem) {

		Font fonteRodape = super.getFonteSelecionada().deriveFont(Font.ITALIC,
				(float) (imagem.getWidth() * super.getProporcaoFonteDeRodape()));

		return fonteRodape;

	}

}