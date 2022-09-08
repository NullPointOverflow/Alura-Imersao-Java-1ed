package br.com.imersao.java.factory.sticker.componente;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import br.com.imersao.java.exception.GeracaoDeStickerException;
import br.com.imersao.java.util.CalculadorDeImagem;

public class ImagemFactory implements CalculadorDeImagem {

	public BufferedImage gerar(InputStream pseudoImagem) throws IOException {

		BufferedImage imagem;

		try {

			imagem = ImageIO.read(pseudoImagem);

			pseudoImagem.close();

		} catch (IOException e) {

			throw new GeracaoDeStickerException("Não foi possível gerar a imagem.", e);

		}

		return imagem;

	}

	public BufferedImage gerar(int largura, int altura) {

		BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TRANSLUCENT);

		return imagem;

	}

	public BufferedImage redimensionar(BufferedImage imagemOriginal) {

		int alturaRedimensionada = 500;

		int larguraRedimensionada = this.arredondarValor(alturaRedimensionada * this.calcularPropocao(imagemOriginal));

		BufferedImage imagemRedimensionada = new BufferedImage(larguraRedimensionada, alturaRedimensionada,
				imagemOriginal.getType());

		return imagemRedimensionada;

	}

}
