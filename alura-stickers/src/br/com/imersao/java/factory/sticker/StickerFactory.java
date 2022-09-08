package br.com.imersao.java.factory.sticker;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import br.com.imersao.java.exception.GeracaoDeStickerException;
import br.com.imersao.java.record.Conteudo;

public class StickerFactory {

	private static final String PASTA_DE_FIGURINHAS = "sticker/";
	private Font fontePadrao;
	private StickerHorizontalFactory geradorHorizontal;
	private StickerVerticalFactory geradorVertical;
	private BufferedImage imagemDefinitiva;

	public StickerFactory() {

		try (InputStream dadosDaFonte = StickerFactory.class.getResourceAsStream("/impact.ttf")) {

			this.fontePadrao = Font.createFont(Font.TRUETYPE_FONT, dadosDaFonte);

		} catch (IOException | FontFormatException e) {

			this.fontePadrao = new Font(Font.MONOSPACED, Font.BOLD, 1);

		}

		this.geradorHorizontal = new StickerHorizontalFactory(fontePadrao);

		this.geradorVertical = new StickerVerticalFactory(fontePadrao);

	}

	public void gerar(InputStream imagemBinaria, Conteudo conteudo) throws GeracaoDeStickerException {

		try {

			BufferedImage imagemOriginal = ImageIO.read(imagemBinaria);

			if (imagemOriginal.getHeight() >= imagemOriginal.getWidth()) {

				this.imagemDefinitiva = this.geradorVertical.gerar(imagemOriginal, conteudo);

			} else {

				this.imagemDefinitiva = this.geradorHorizontal.gerar(imagemOriginal, conteudo);

			}

			File pastaDeFigurinhas = new File(PASTA_DE_FIGURINHAS);

			if (!(pastaDeFigurinhas.exists())) {

				Files.createDirectories(Paths.get(PASTA_DE_FIGURINHAS));

			}

			ImageIO.write(this.imagemDefinitiva, "PNG", new File(PASTA_DE_FIGURINHAS + conteudo.titulo() + ".png"));

		} catch (IOException e) {

			throw new GeracaoDeStickerException("Não foi possível gerar a figurinha com a imagem informada.", e);

		}

	}

}
