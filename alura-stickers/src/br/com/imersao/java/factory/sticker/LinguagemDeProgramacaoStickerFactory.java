package br.com.imersao.java.factory.sticker;

import java.awt.Canvas;
import java.awt.Graphics2D;
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
import br.com.imersao.java.factory.sticker.componente.CanetaFactory;
import br.com.imersao.java.factory.sticker.componente.ImagemFactory;
import br.com.imersao.java.factory.sticker.componente.fonte.FonteFactory;
import br.com.imersao.java.factory.sticker.componente.fonte.ImagemVerticalFonteFactory;
import br.com.imersao.java.factory.sticker.handler.classificao.ClassificacaoHandler;
import br.com.imersao.java.factory.sticker.handler.classificao.ClassificacaoOutros;
import br.com.imersao.java.factory.sticker.handler.classificao.ClassificacaoPrimeiro;
import br.com.imersao.java.factory.sticker.handler.classificao.ClassificacaoSegundo;
import br.com.imersao.java.factory.sticker.handler.classificao.ClassificacaoTerceiro;
import br.com.imersao.java.util.Arredondamento;

public class LinguagemDeProgramacaoStickerFactory extends StickerFactory implements Arredondamento {

	private ClassificacaoHandler classificacaoHandler;
	private ImagemFactory geradorDeImagem;
	private CanetaFactory geradorDeCaneta = new CanetaFactory();
	private FonteFactory geradorDeFonte = new ImagemVerticalFonteFactory();

	private Graphics2D caneta;

	private BufferedImage imagemOriginal;
	private BufferedImage imagemRedimensionada;
	private BufferedImage imagemDefinitiva;

	public LinguagemDeProgramacaoStickerFactory() {

		this.classificacaoHandler = new ClassificacaoPrimeiro(
				new ClassificacaoSegundo(new ClassificacaoTerceiro(new ClassificacaoOutros())));

		this.geradorDeImagem = new ImagemFactory();

		this.geradorDeFonte.setProporcaoFonteDeCabecalho(0.12);

		this.geradorDeFonte.setProporcaoFonteDeRodape(0.08);

	}

	private void salvarSticker(BufferedImage imagem, String titulo) throws IOException {

		String diretorio = super.getDiretorioDeDestino() + "linguagem/";

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
								+ this.arredondarValor(this.imagemRedimensionada.getHeight() * 0.5)));

				this.caneta = this.geradorDeCaneta.gerar(this.imagemDefinitiva);

				this.caneta.drawImage(this.imagemRedimensionada, 0,
						this.arredondarValor(
								(this.imagemDefinitiva.getHeight() - this.imagemRedimensionada.getHeight()) * 0.6),
						new Canvas());

				this.caneta.dispose();

				this.classificacaoHandler.manipular(this.geradorDeFonte, this.imagemDefinitiva, listaDeConteudo.get(i),
						(i + 1));

				this.salvarSticker(imagemDefinitiva, listaDeConteudo.get(i).titulo());

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
