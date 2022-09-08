package br.com.imersao.java.factory.sticker.componente;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ContornoFactory {

	public void gerar(BufferedImage imagem, Graphics2D caneta, String texto, int eixoX, int eixoY) {

		FontRenderContext contextoDaFonte = caneta.getFontRenderContext();

		TextLayout layoutDoTexto = new TextLayout(texto, caneta.getFont(), contextoDaFonte);

		AffineTransform transformador = caneta.getTransform();

		Shape contorno = layoutDoTexto.getOutline(transformador);

		transformador.translate(eixoX, eixoY);

		caneta.transform(transformador);

		caneta.setColor(Color.BLACK);

		caneta.draw(contorno);

		caneta.setClip(contorno);

	}

}
