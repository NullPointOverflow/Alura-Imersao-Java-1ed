package br.com.imersao.java.factory.sticker;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.exception.ConexaoWebException;
import br.com.imersao.java.factory.sticker.componente.CanetaFactory;
import br.com.imersao.java.factory.sticker.componente.ImagemFactory;
import br.com.imersao.java.util.Arredondamento;
import br.com.imersao.java.util.Default;

public class NasaStickerFactory extends StickerFactory implements Arredondamento {

	private BufferedImage logoNasa;
	private ImagemFactory geradorDeImagem;
	private CanetaFactory geradorDeCaneta = new CanetaFactory();

	private BufferedImage imagemOriginal;
	private BufferedImage imagemRedimensionada;
	private BufferedImage imagemDefinitiva;

	private Graphics2D caneta;

	public NasaStickerFactory() {

		try (InputStream psudoLogo = MarvelStickerFactory.class.getClassLoader()
				.getResourceAsStream("logo/nasa_logo.png")) {

			this.logoNasa = ImageIO.read(psudoLogo);

		} catch (IOException | IllegalArgumentException e) {

			this.logoNasa = Default.getEmptyImage();

		}

		this.geradorDeImagem = new ImagemFactory();

	}

	private void salvarSticker(BufferedImage imagem, String titulo) throws IOException {

		String diretorio = super.getDiretorioDeDestino() + "nasa/";

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

				this.imagemRedimensionada = this.geradorDeImagem.redimensionar(this.imagemOriginal);

				this.caneta = this.geradorDeCaneta.gerar(this.imagemRedimensionada);

				this.caneta.drawImage(this.imagemOriginal, 0, 0, this.imagemRedimensionada.getWidth(),
						this.imagemRedimensionada.getHeight(), new Canvas());

				this.caneta.dispose();

				this.imagemDefinitiva = this.geradorDeImagem.gerar(this.imagemRedimensionada.getWidth(),
						(this.imagemRedimensionada.getHeight()
								+ this.arredondarValor(this.imagemRedimensionada.getHeight() * 0.2)));

				this.caneta = this.geradorDeCaneta.gerar(this.imagemDefinitiva);

				this.caneta.drawImage(this.imagemRedimensionada, 0, 0, new Canvas());

				this.caneta.drawImage(this.logoNasa,
						this.arredondarValor(
								(this.imagemDefinitiva.getWidth() * 0.5) - (this.logoNasa.getWidth() * 0.5)),
						515, new Canvas());

				this.caneta.dispose();

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
