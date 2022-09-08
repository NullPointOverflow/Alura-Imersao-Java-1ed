package br.com.imersao.java.factory.sticker.imdb;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.exception.ConexaoWebException;
import br.com.imersao.java.factory.sticker.StickerFactory;
import br.com.imersao.java.factory.sticker.componente.ImagemFactory;
import br.com.imersao.java.factory.sticker.handler.avaliacao.AvaliacaoBom;
import br.com.imersao.java.factory.sticker.handler.avaliacao.AvaliacaoEspetacular;
import br.com.imersao.java.factory.sticker.handler.avaliacao.AvaliacaoHandler;
import br.com.imersao.java.factory.sticker.handler.avaliacao.AvaliacaoMediano;
import br.com.imersao.java.factory.sticker.handler.avaliacao.AvaliacaoRuim;

public class ImdbStickerFactory extends StickerFactory {

	private AvaliacaoHandler handlerSequencial;
	private ImagemFactory geradorDeImagem;
	private ImdbHorizontalStickerFactory geradorHorizontal;
	private ImdbVerticalStickerFactory geradorVertical;
	private BufferedImage imagemOriginal;
	private BufferedImage imagemDefinitiva;

	public ImdbStickerFactory() {

		this.handlerSequencial = new AvaliacaoEspetacular(new AvaliacaoBom(new AvaliacaoMediano(new AvaliacaoRuim())));

		this.geradorDeImagem = new ImagemFactory();

		this.geradorHorizontal = new ImdbHorizontalStickerFactory(this.geradorDeImagem, this.handlerSequencial);

		this.geradorVertical = new ImdbVerticalStickerFactory(this.geradorDeImagem, this.handlerSequencial);

	}

	private void salvarSticker(BufferedImage imagem, String titulo) throws IOException {

		String diretorio = super.getDiretorioDeDestino() + "imdb/";

		File pastaDeFigurinhas = new File(diretorio);

		if (!(pastaDeFigurinhas.exists())) {

			Files.createDirectories(Paths.get(diretorio));

		}

		ImageIO.write(imagem, "PNG", new File(diretorio + titulo + ".png"));

	}

	@Override
	public Map<String, Integer> gerar(List<Conteudo> listaDeConteudo) {

		int sucessos = 0;
		int falhas = 0;
		Map<String, Integer> statusDeGeracao = new HashMap<String, Integer>(2);

		for (int i = 0; i < listaDeConteudo.size(); i++) {

			try {

				this.imagemOriginal = this.geradorDeImagem
						.gerar(this.getClienteHTTP().abrirFluxoDeDados(listaDeConteudo.get(i).uriImagem()));

				if (this.imagemOriginal.getHeight() >= this.imagemOriginal.getWidth()) {

					this.imagemDefinitiva = this.geradorVertical.gerar(this.imagemOriginal, listaDeConteudo.get(i));

				} else {

					this.imagemDefinitiva = this.geradorHorizontal.gerar(this.imagemOriginal, listaDeConteudo.get(i));

				}

				this.salvarSticker(this.imagemDefinitiva, listaDeConteudo.get(i).titulo());

				sucessos++;

			} catch (IOException | ConexaoWebException e) {

				falhas++;

			}

		}

		statusDeGeracao.put("sucessos", sucessos);

		statusDeGeracao.put("falhas", falhas);

		return statusDeGeracao;

	}

}
