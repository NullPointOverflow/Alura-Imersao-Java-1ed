
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class StickerFactory {

	private static final String PASTA_DE_FIGURINHAS = "sticker/";
	private static Font fonteEstrelas = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private Font fontePadrao;
	private int alturaOriginal;
	private int larguraOriginal;
	private int alturaRedimensionada;
	private int larguraRedimensionada;
	private int alturaDefinitiva;
	private int larguraDefinitiva;
	private BufferedImage imagemDefinitiva;
	private Graphics2D caneta;
	private Font fonteAvaliacao;
	private Font fonteNota;
	private Font fonteClassificacao;
	private int eixoX;
	private int eixoY;
	private String fraseAvaliacao = "AVALIAÇÃO:";
	private StringBuilder fraseEstrelas = new StringBuilder();
	private Classificacao classificacao;
	private Color corDoContorno = Color.BLACK;
	private FontRenderContext contextoDaFonte;
	private TextLayout layoutDoTexto;
	private AffineTransform transformador;
	private Shape contorno;

	public StickerFactory(String nomeDaFonte) {

		try (InputStream dadosDaFonte = StickerFactory.class.getResourceAsStream(nomeDaFonte)) {

			this.fontePadrao = Font.createFont(Font.TRUETYPE_FONT, dadosDaFonte);

		} catch (IOException | FontFormatException e) {

			this.fontePadrao = fonteEstrelas;

		}

	}

	public void gerar(InputStream imagemBinaria, String nomeParaImagem, int avaliacao) throws IOException {

		// Ler uma imagem
		BufferedImage imagemOriginal = ImageIO.read(imagemBinaria);
		this.alturaOriginal = imagemOriginal.getHeight();
		this.larguraOriginal = imagemOriginal.getWidth();
		double proporcao = (1.0 * this.larguraOriginal) / this.alturaOriginal;

		// Criar nova imagem em memória com transparencia e novas dimensões
		this.alturaRedimensionada = 500;
		this.larguraRedimensionada = (int) (this.alturaRedimensionada * proporcao);

		BufferedImage imagemRedimensionada = new BufferedImage(this.larguraRedimensionada, this.alturaRedimensionada,
				imagemOriginal.getType());

		imagemRedimensionada.createGraphics().drawImage(imagemOriginal, 0, 0, this.larguraRedimensionada,
				this.alturaRedimensionada, new Canvas());

		this.alturaDefinitiva = this.alturaRedimensionada + (int) (this.alturaRedimensionada * 0.4);
		this.larguraDefinitiva = this.larguraRedimensionada;

		this.imagemDefinitiva = new BufferedImage(this.larguraDefinitiva, this.alturaDefinitiva,
				BufferedImage.TRANSLUCENT);

		// Aplicar a imagem original sobre a imagem em memória
		this.caneta = this.imagemDefinitiva.createGraphics();

		this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		this.caneta.drawImage(imagemRedimensionada, 0,
				(int) ((this.alturaDefinitiva - this.alturaRedimensionada) * 0.65), new Canvas());

		this.caneta.dispose();

		// Configurar uma frase
		if (this.alturaOriginal >= this.larguraOriginal) {

			this.fonteAvaliacao = this.fontePadrao.deriveFont(Font.PLAIN, (int) (this.alturaDefinitiva * 0.06));
			this.fonteNota = fonteEstrelas.deriveFont(Font.BOLD, (int) (this.larguraDefinitiva * 0.161));
			this.fonteClassificacao = this.fontePadrao.deriveFont(Font.ITALIC, (int) (this.larguraDefinitiva * 0.15));

		} else {

			this.fonteAvaliacao = this.fontePadrao.deriveFont(Font.PLAIN, (int) (this.alturaDefinitiva * 0.06));
			this.fonteNota = fonteEstrelas.deriveFont(Font.BOLD, (int) (this.alturaDefinitiva * 0.161));
			this.fonteClassificacao = this.fontePadrao.deriveFont(Font.ITALIC, (int) (this.alturaDefinitiva * 0.11));

		}

		// Escrever a frase na nova imagem
		if (avaliacao >= 8) {

			this.classificacao = Classificacao.ESPETACULAR;

			this.caneta = this.imagemDefinitiva.createGraphics();

			this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			this.caneta.setFont(this.fonteAvaliacao);
			this.caneta.setColor(this.classificacao.getCor());

			this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
					- (this.caneta.getFontMetrics().stringWidth(this.fraseAvaliacao) * 0.5));
			this.eixoY = (int) (this.alturaDefinitiva * 0.06);

			this.caneta.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

			this.contextoDaFonte = this.caneta.getFontRenderContext();
			this.layoutDoTexto = new TextLayout(this.fraseAvaliacao, this.caneta.getFont(), this.contextoDaFonte);
			this.transformador = this.caneta.getTransform();
			this.contorno = this.layoutDoTexto.getOutline(this.transformador);
			this.transformador.translate(this.eixoX, this.eixoY);

			this.caneta.transform(this.transformador);
			this.caneta.setColor(this.corDoContorno);
			this.caneta.draw(this.contorno);
			this.caneta.setClip(this.contorno);
			this.caneta.dispose();

			this.caneta = imagemDefinitiva.createGraphics();

			this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			this.caneta.setFont(this.fonteNota);
			this.caneta.setColor(this.classificacao.getCor());

			this.fraseEstrelas.setLength(0);

			for (int i = 0; i < avaliacao; i++) {

				this.fraseEstrelas.append(SimboloUnicode.ESTRELA.getSimbolo());

			}

			this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
					- (this.caneta.getFontMetrics().stringWidth(this.fraseEstrelas.toString()) * 0.5));

			if (this.alturaOriginal >= this.larguraOriginal) {

				this.eixoY = (int) (this.alturaDefinitiva * 0.17);

			} else {

				this.eixoY = (int) (this.alturaDefinitiva * 0.18);

			}

			this.caneta.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

			this.contextoDaFonte = this.caneta.getFontRenderContext();
			this.layoutDoTexto = new TextLayout(this.fraseEstrelas.toString(), this.caneta.getFont(),
					this.contextoDaFonte);
			this.transformador = this.caneta.getTransform();
			this.contorno = this.layoutDoTexto.getOutline(this.transformador);
			this.transformador.translate(this.eixoX, this.eixoY);

			this.caneta.transform(this.transformador);
			this.caneta.setColor(this.corDoContorno);
			this.caneta.draw(this.contorno);
			this.caneta.setClip(this.contorno);
			this.caneta.dispose();

			this.caneta = this.imagemDefinitiva.createGraphics();

			this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			this.caneta.setFont(this.fonteClassificacao);
			this.caneta.setColor(this.classificacao.getCor());

			this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
					- (this.caneta.getFontMetrics().stringWidth(this.classificacao.getString()) * 0.5));
			this.eixoY = (int) (this.alturaDefinitiva * 0.995);

			this.caneta.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

			this.contextoDaFonte = this.caneta.getFontRenderContext();
			this.layoutDoTexto = new TextLayout(this.classificacao.getString(), this.caneta.getFont(),
					this.contextoDaFonte);
			this.transformador = this.caneta.getTransform();
			this.contorno = this.layoutDoTexto.getOutline(transformador);
			this.transformador.translate(this.eixoX, this.eixoY);

			this.caneta.transform(this.transformador);
			this.caneta.setColor(this.corDoContorno);
			this.caneta.draw(this.contorno);
			this.caneta.setClip(this.contorno);
			this.caneta.dispose();

		} else {

			if (avaliacao < 8 && avaliacao > 5) {

				this.classificacao = Classificacao.BOM;

				this.caneta = this.imagemDefinitiva.createGraphics();

				this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				this.caneta.setFont(this.fonteAvaliacao);
				this.caneta.setColor(this.classificacao.getCor());

				this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
						- (this.caneta.getFontMetrics().stringWidth(this.fraseAvaliacao) * 0.5));
				this.eixoY = (int) (this.alturaDefinitiva * 0.06);

				this.caneta.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

				this.contextoDaFonte = this.caneta.getFontRenderContext();
				this.layoutDoTexto = new TextLayout(this.fraseAvaliacao, this.caneta.getFont(), this.contextoDaFonte);
				this.transformador = this.caneta.getTransform();
				this.contorno = this.layoutDoTexto.getOutline(this.transformador);
				this.transformador.translate(this.eixoX, this.eixoY);

				this.caneta.transform(this.transformador);
				this.caneta.setColor(this.corDoContorno);
				this.caneta.draw(this.contorno);
				this.caneta.setClip(this.contorno);
				this.caneta.dispose();

				this.caneta = this.imagemDefinitiva.createGraphics();

				this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				this.caneta.setFont(this.fonteNota);
				this.caneta.setColor(this.classificacao.getCor());

				this.fraseEstrelas.setLength(0);

				for (int i = 0; i < avaliacao; i++) {

					this.fraseEstrelas.append(SimboloUnicode.ESTRELA.getSimbolo());

				}

				this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
						- (this.caneta.getFontMetrics().stringWidth(this.fraseEstrelas.toString()) * 0.5));

				if (this.alturaOriginal >= this.larguraOriginal) {

					this.eixoY = (int) (this.alturaDefinitiva * 0.17);

				} else {

					this.eixoY = (int) (this.alturaDefinitiva * 0.18);

				}

				this.caneta.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

				this.contextoDaFonte = this.caneta.getFontRenderContext();
				this.layoutDoTexto = new TextLayout(this.fraseEstrelas.toString(), this.caneta.getFont(),
						this.contextoDaFonte);
				this.transformador = this.caneta.getTransform();
				this.contorno = this.layoutDoTexto.getOutline(this.transformador);
				this.transformador.translate(this.eixoX, this.eixoY);

				this.caneta.transform(this.transformador);
				this.caneta.setColor(this.corDoContorno);
				this.caneta.draw(this.contorno);
				this.caneta.setClip(this.contorno);
				this.caneta.dispose();

				this.caneta = this.imagemDefinitiva.createGraphics();

				this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				this.caneta.setFont(this.fonteClassificacao);
				this.caneta.setColor(this.classificacao.getCor());

				this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
						- (this.caneta.getFontMetrics().stringWidth(this.classificacao.getString()) * 0.5));
				this.eixoY = (int) (this.alturaDefinitiva * 0.995);

				this.caneta.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

				this.contextoDaFonte = this.caneta.getFontRenderContext();
				this.layoutDoTexto = new TextLayout(this.classificacao.getString(), this.caneta.getFont(),
						this.contextoDaFonte);
				this.transformador = this.caneta.getTransform();
				this.contorno = this.layoutDoTexto.getOutline(this.transformador);
				this.transformador.translate(this.eixoX, this.eixoY);

				this.caneta.transform(this.transformador);
				this.caneta.setColor(this.corDoContorno);
				this.caneta.draw(this.contorno);
				this.caneta.setClip(this.contorno);
				this.caneta.dispose();

			} else {

				if (avaliacao == 5) {

					this.classificacao = Classificacao.MEDIANO;

					this.caneta = this.imagemDefinitiva.createGraphics();

					this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					this.caneta.setFont(this.fonteAvaliacao);
					this.caneta.setColor(this.classificacao.getCor());

					this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
							- (this.caneta.getFontMetrics().stringWidth(this.fraseAvaliacao) * 0.5));
					this.eixoY = (int) (this.alturaDefinitiva * 0.06);

					this.caneta.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

					this.contextoDaFonte = this.caneta.getFontRenderContext();
					this.layoutDoTexto = new TextLayout(this.fraseAvaliacao, this.caneta.getFont(),
							this.contextoDaFonte);
					this.transformador = this.caneta.getTransform();
					this.contorno = this.layoutDoTexto.getOutline(this.transformador);
					this.transformador.translate(this.eixoX, this.eixoY);

					this.caneta.transform(this.transformador);
					this.caneta.setColor(this.corDoContorno);
					this.caneta.draw(this.contorno);
					this.caneta.setClip(this.contorno);
					this.caneta.dispose();

					this.caneta = this.imagemDefinitiva.createGraphics();

					this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					this.caneta.setFont(this.fonteNota);
					this.caneta.setColor(this.classificacao.getCor());

					this.fraseEstrelas.setLength(0);

					for (int i = 0; i < avaliacao; i++) {

						this.fraseEstrelas.append(SimboloUnicode.ESTRELA.getSimbolo());

					}

					this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
							- (this.caneta.getFontMetrics().stringWidth(this.fraseEstrelas.toString()) * 0.5));

					if (this.alturaOriginal >= this.larguraOriginal) {

						this.eixoY = (int) (this.alturaDefinitiva * 0.17);

					} else {

						this.eixoY = (int) (this.alturaDefinitiva * 0.18);

					}

					this.caneta.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

					this.contextoDaFonte = this.caneta.getFontRenderContext();
					this.layoutDoTexto = new TextLayout(this.fraseEstrelas.toString(), this.caneta.getFont(),
							this.contextoDaFonte);
					this.transformador = this.caneta.getTransform();
					this.contorno = this.layoutDoTexto.getOutline(this.transformador);
					this.transformador.translate(this.eixoX, this.eixoY);

					this.caneta.transform(this.transformador);
					this.caneta.setColor(this.corDoContorno);
					this.caneta.draw(this.contorno);
					this.caneta.setClip(this.contorno);
					this.caneta.dispose();

					this.caneta = this.imagemDefinitiva.createGraphics();

					this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					this.caneta.setFont(this.fonteClassificacao);
					this.caneta.setColor(this.classificacao.getCor());

					this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
							- (this.caneta.getFontMetrics().stringWidth(this.classificacao.getString()) * 0.5));
					this.eixoY = (int) (this.alturaDefinitiva * 0.995);

					this.caneta.drawString(this.classificacao.getString(), eixoX, eixoY);

					this.contextoDaFonte = this.caneta.getFontRenderContext();
					this.layoutDoTexto = new TextLayout(this.classificacao.getString(), this.caneta.getFont(),
							this.contextoDaFonte);
					this.transformador = this.caneta.getTransform();
					this.contorno = this.layoutDoTexto.getOutline(this.transformador);
					this.transformador.translate(this.eixoX, this.eixoY);

					this.caneta.transform(this.transformador);
					this.caneta.setColor(this.corDoContorno);
					this.caneta.draw(this.contorno);
					this.caneta.setClip(this.contorno);
					this.caneta.dispose();

				} else {

					if (avaliacao > 0) {

						this.classificacao = Classificacao.RUIM;

						this.caneta = this.imagemDefinitiva.createGraphics();

						this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
						this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						this.caneta.setFont(this.fonteAvaliacao);
						this.caneta.setColor(this.classificacao.getCor());

						this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
								- (this.caneta.getFontMetrics().stringWidth(this.fraseAvaliacao) * 0.5));
						this.eixoY = (int) (this.alturaDefinitiva * 0.06);

						this.caneta.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

						this.contextoDaFonte = this.caneta.getFontRenderContext();
						this.layoutDoTexto = new TextLayout(this.fraseAvaliacao, this.caneta.getFont(),
								this.contextoDaFonte);
						this.transformador = this.caneta.getTransform();
						this.contorno = this.layoutDoTexto.getOutline(this.transformador);
						this.transformador.translate(this.eixoX, this.eixoY);

						this.caneta.transform(this.transformador);
						this.caneta.setColor(this.corDoContorno);
						this.caneta.draw(this.contorno);
						this.caneta.setClip(this.contorno);
						this.caneta.dispose();

						this.caneta = this.imagemDefinitiva.createGraphics();

						this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
						this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						this.caneta.setFont(this.fonteNota);
						this.caneta.setColor(this.classificacao.getCor());

						this.fraseEstrelas.setLength(0);

						for (int i = 0; i < avaliacao; i++) {

							this.fraseEstrelas.append(SimboloUnicode.ESTRELA.getSimbolo());

						}

						this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
								- (this.caneta.getFontMetrics().stringWidth(this.fraseEstrelas.toString()) * 0.5));

						if (this.alturaOriginal >= this.larguraOriginal) {

							this.eixoY = (int) (this.alturaDefinitiva * 0.17);

						} else {

							this.eixoY = (int) (this.alturaDefinitiva * 0.18);

						}

						this.caneta.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

						this.contextoDaFonte = this.caneta.getFontRenderContext();
						this.layoutDoTexto = new TextLayout(this.fraseEstrelas.toString(), this.caneta.getFont(),
								this.contextoDaFonte);
						this.transformador = this.caneta.getTransform();
						this.contorno = this.layoutDoTexto.getOutline(this.transformador);
						this.transformador.translate(this.eixoX, this.eixoY);

						this.caneta.transform(this.transformador);
						this.caneta.setColor(this.corDoContorno);
						this.caneta.draw(this.contorno);
						this.caneta.setClip(this.contorno);
						this.caneta.dispose();

						this.caneta = this.imagemDefinitiva.createGraphics();

						this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
						this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						this.caneta.setFont(this.fonteClassificacao);
						this.caneta.setColor(this.classificacao.getCor());

						this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
								- (this.caneta.getFontMetrics().stringWidth(this.classificacao.getString()) * 0.5));
						this.eixoY = (int) (this.alturaDefinitiva * 0.995);

						this.caneta.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

						this.contextoDaFonte = this.caneta.getFontRenderContext();
						this.layoutDoTexto = new TextLayout(this.classificacao.getString(), this.caneta.getFont(),
								this.contextoDaFonte);
						this.transformador = this.caneta.getTransform();
						this.contorno = this.layoutDoTexto.getOutline(this.transformador);
						this.transformador.translate(this.eixoX, this.eixoY);

						this.caneta.transform(this.transformador);
						this.caneta.setColor(this.corDoContorno);
						this.caneta.draw(this.contorno);
						this.caneta.setClip(this.contorno);
						this.caneta.dispose();

					} else {

						this.classificacao = Classificacao.NAO_AVALIADO;

						this.caneta = this.imagemDefinitiva.createGraphics();

						this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
						this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						this.caneta.setFont(this.fonteAvaliacao);
						this.caneta.setColor(this.classificacao.getCor());

						this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
								- (this.caneta.getFontMetrics().stringWidth(this.fraseAvaliacao) * 0.5));
						this.eixoY = (int) (this.alturaDefinitiva * 0.06);

						this.caneta.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

						this.contextoDaFonte = this.caneta.getFontRenderContext();
						this.layoutDoTexto = new TextLayout(this.fraseAvaliacao, this.caneta.getFont(),
								this.contextoDaFonte);
						this.transformador = this.caneta.getTransform();
						this.contorno = this.layoutDoTexto.getOutline(this.transformador);
						this.transformador.translate(this.eixoX, this.eixoY);

						this.caneta.transform(this.transformador);
						this.caneta.setColor(this.corDoContorno);
						this.caneta.draw(this.contorno);
						this.caneta.setClip(this.contorno);
						this.caneta.dispose();

						this.caneta = imagemDefinitiva.createGraphics();

						this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
						this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						this.caneta.setFont(this.fonteAvaliacao);
						this.caneta.setColor(this.classificacao.getCor());

						this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
								- (this.caneta.getFontMetrics().stringWidth(this.classificacao.getString()) * 0.5));

						if (this.alturaOriginal >= this.larguraOriginal) {

							this.eixoY = (int) (this.alturaDefinitiva * 0.17);

						} else {

							this.eixoY = (int) (this.alturaDefinitiva * 0.18);

						}

						this.caneta.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

						this.contextoDaFonte = this.caneta.getFontRenderContext();
						this.layoutDoTexto = new TextLayout(this.classificacao.getString(), this.caneta.getFont(),
								this.contextoDaFonte);
						this.transformador = this.caneta.getTransform();
						this.contorno = this.layoutDoTexto.getOutline(this.transformador);
						this.transformador.translate(this.eixoX, this.eixoY);

						this.caneta.transform(this.transformador);
						this.caneta.setColor(this.corDoContorno);
						this.caneta.draw(this.contorno);
						this.caneta.setClip(this.contorno);
						this.caneta.dispose();

						this.caneta = this.imagemDefinitiva.createGraphics();

						this.caneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						this.caneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
						this.caneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						this.caneta.setFont(this.fonteClassificacao);
						this.caneta.setColor(this.classificacao.getCor());

						this.eixoX = (int) ((this.larguraDefinitiva * 0.5)
								- (this.caneta.getFontMetrics().stringWidth(nomeParaImagem) * 0.5));
						this.eixoY = (int) (this.alturaDefinitiva * 0.995);

						this.caneta.drawString(nomeParaImagem, this.eixoX, this.eixoY);

						this.contextoDaFonte = this.caneta.getFontRenderContext();
						this.layoutDoTexto = new TextLayout(nomeParaImagem, this.caneta.getFont(),
								this.contextoDaFonte);
						this.transformador = this.caneta.getTransform();
						this.contorno = this.layoutDoTexto.getOutline(this.transformador);
						this.transformador.translate(this.eixoX, this.eixoY);

						this.caneta.transform(this.transformador);
						this.caneta.setColor(this.corDoContorno);
						this.caneta.draw(this.contorno);
						this.caneta.setClip(this.contorno);
						this.caneta.dispose();

					}

				}

			}

		}

		// Escrever a nova imagem em arquivo
		File pastaDeFigurinhas = new File(PASTA_DE_FIGURINHAS);

		if (!(pastaDeFigurinhas.exists())) {

			Files.createDirectories(Paths.get(PASTA_DE_FIGURINHAS));

		}

		ImageIO.write(this.imagemDefinitiva, "PNG", new File(PASTA_DE_FIGURINHAS + nomeParaImagem + ".png"));

	}

}
