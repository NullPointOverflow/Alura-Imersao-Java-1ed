package br.com.imersao.java.factory.sticker.imdb;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.factory.sticker.componente.CanetaFactory;
import br.com.imersao.java.factory.sticker.componente.ImagemFactory;
import br.com.imersao.java.factory.sticker.componente.fonte.FonteFactory;
import br.com.imersao.java.factory.sticker.componente.fonte.ImagemHorizontalFonteFactory;
import br.com.imersao.java.factory.sticker.handler.avaliacao.AvaliacaoHandler;
import br.com.imersao.java.util.Arredondamento;

public class ImdbHorizontalStickerFactory implements Arredondamento {

	private ImagemFactory geradorDeImagem;
	private CanetaFactory geradorDeCaneta = new CanetaFactory();
	private FonteFactory geradorDeFonte = new ImagemHorizontalFonteFactory();
	private AvaliacaoHandler avaliacaoHandler;

	private Graphics2D caneta;

	private BufferedImage imagemOriginal;
	private BufferedImage imagemRedimensionada;
	private BufferedImage imagemDefinitiva;

	public ImdbHorizontalStickerFactory(ImagemFactory geradorDeImagem, AvaliacaoHandler avaliacaoHandler) {

		this.geradorDeFonte.setProporcaoFonteDeCabecalho(0.06);

		this.geradorDeFonte.setProporcaoFonteDeCentro(0.12);

		this.geradorDeFonte.setProporcaoFonteDeRodape(0.11);

		this.geradorDeImagem = geradorDeImagem;

		this.avaliacaoHandler = avaliacaoHandler;

	}

	public BufferedImage gerar(BufferedImage imagem, Conteudo conteudo) throws IOException {

		this.imagemOriginal = imagem;

		this.imagemRedimensionada = this.geradorDeImagem.redimensionar(this.imagemOriginal);

		this.caneta = this.geradorDeCaneta.gerar(this.imagemRedimensionada);

		this.caneta.drawImage(this.imagemOriginal, 0, 0, this.imagemRedimensionada.getWidth(),
				this.imagemRedimensionada.getHeight(), new Canvas());

		this.caneta.dispose();

		this.imagemDefinitiva = this.geradorDeImagem.gerar(this.imagemRedimensionada.getWidth(),
				(this.imagemRedimensionada.getHeight() + arredondarValor(this.imagemRedimensionada.getHeight() * 0.4)));

		this.caneta = this.geradorDeCaneta.gerar(this.imagemDefinitiva);

		this.caneta.drawImage(this.imagemRedimensionada, 0,
				this.arredondarValor(
						(this.imagemDefinitiva.getHeight() - this.imagemRedimensionada.getHeight()) * 0.65),
				new Canvas());

		this.caneta.dispose();

		this.avaliacaoHandler.manipular(this.geradorDeFonte, this.imagemDefinitiva, conteudo);

		return this.imagemDefinitiva;

	}

}
