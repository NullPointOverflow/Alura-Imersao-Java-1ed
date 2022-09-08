package br.com.imersao.java.util;

import java.awt.image.BufferedImage;

public interface CalculadorDeImagem extends Arredondamento {

	default double calcularPropocao(BufferedImage imagem) {

		double proporcao = (1.0 * imagem.getWidth()) / imagem.getHeight();

		return proporcao;

	}

	default int calcularPosicaoNoEixoY(BufferedImage imagem, double variacaoDeAltura) {

		int eixoY = this.arredondarValor(imagem.getHeight() * variacaoDeAltura);

		return eixoY;

	}

	default int calcularPosicaoNoEixoX(BufferedImage imagem, int larguraDoElementoGrafico) {

		int eixoX = this.arredondarValor(imagem.getWidth() * 0.5)
				- this.arredondarValor(larguraDoElementoGrafico * 0.5);

		return eixoX;

	}

}
