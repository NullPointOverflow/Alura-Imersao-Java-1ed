package br.com.imersao.java.factory.sticker;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.stream.IntStream;

import br.com.imersao.java.enumeration.Classificacao;
import br.com.imersao.java.enumeration.SimboloUnicode;
import br.com.imersao.java.record.Conteudo;

public class StickerHorizontalFactory {

	private BufferedImage imagemOriginal;
	private BufferedImage imagemRedimensionada;
	private BufferedImage imagemDefinitiva;

	private int alturaOriginal;
	private int larguraOriginal;
	private int alturaRedimensionada;
	private int larguraRedimensionada;
	private int alturaDefinitiva;
	private int larguraDefinitiva;
	private int eixoX;
	private int eixoY;

	private Graphics2D canetaDeDesenho;

	private Classificacao classificacao;

	private Font fontePadrao;
	private Font fonteAvaliacao;
	private Font fonteEstrelas;
	private Font fonteClassificacao;

	private String fraseAvaliacao = "AVALIAÇÃO:";
	private StringBuilder fraseEstrelas;

	public StickerHorizontalFactory(Font fontePadrao) {

		this.fontePadrao = fontePadrao;

	}

	public void setNotaEmFormatoSimbolo(int avaliacao) {

		this.fraseEstrelas = new StringBuilder(10);

		IntStream.range(0, avaliacao)
				.forEach(inteiro -> this.fraseEstrelas.append(SimboloUnicode.ESTRELA.getSimbolo()));

	}

	public void desenharContornoNoTexto(String texto) {

		FontRenderContext contextoDaFonte = this.canetaDeDesenho.getFontRenderContext();

		TextLayout layoutDoTexto = new TextLayout(texto, this.canetaDeDesenho.getFont(), contextoDaFonte);

		AffineTransform transformador = this.canetaDeDesenho.getTransform();

		Shape contorno = layoutDoTexto.getOutline(transformador);

		transformador.translate(this.eixoX, this.eixoY);

		this.canetaDeDesenho.transform(transformador);

		this.canetaDeDesenho.setColor(Color.BLACK);

		this.canetaDeDesenho.draw(contorno);

		this.canetaDeDesenho.setClip(contorno);

	}

	public void calcularEixoX(int larguraDaFrase) {

		this.eixoX = (int) ((this.larguraDefinitiva * 0.5) - (larguraDaFrase * 0.5));

	}

	public void calcularEixoY(double variacaoDeAltura) {

		this.eixoY = (int) (this.alturaDefinitiva * variacaoDeAltura);

	}

	private void setFonteDeTextoDasFrases() {

		this.fonteAvaliacao = this.fontePadrao.deriveFont(Font.PLAIN, (int) (this.alturaDefinitiva * 0.06));

		this.fonteEstrelas = new Font(Font.SANS_SERIF, Font.BOLD, (int) (this.alturaDefinitiva * 0.161));

		this.fonteClassificacao = this.fontePadrao.deriveFont(Font.ITALIC, (int) (this.alturaDefinitiva * 0.11));

	}

	private void setDimensoesDefinitivas() {

		this.alturaDefinitiva = this.alturaRedimensionada + (int) (this.alturaRedimensionada * 0.4);

		this.larguraDefinitiva = this.larguraRedimensionada;

	}

	private void encerrarCanetaDeDesenho() {

		this.canetaDeDesenho.dispose();

	}

	private Graphics2D getCanetaDeDesenho(BufferedImage imagem) {

		Graphics2D novaCaneta = imagem.createGraphics();

		novaCaneta.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		novaCaneta.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		novaCaneta.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		return novaCaneta;

	}

	private BufferedImage redimensionarImagem(double proporcao) {

		this.alturaRedimensionada = 500;

		this.larguraRedimensionada = (int) (this.alturaRedimensionada * proporcao);

		BufferedImage novaImagem = new BufferedImage(this.larguraRedimensionada, this.alturaRedimensionada,
				this.imagemOriginal.getType());

		return novaImagem;

	}

	private double calcularProporcaoDaImagem() {

		double proporcao = (1.0 * this.larguraOriginal) / this.alturaOriginal;

		return proporcao;

	}

	private void setDimensoesOriginais(BufferedImage imagemOriginal) {

		this.alturaOriginal = this.imagemOriginal.getHeight();

		this.larguraOriginal = this.imagemOriginal.getWidth();

	}

	public BufferedImage gerar(BufferedImage imagemOriginal, Conteudo conteudo) throws IOException {

		this.imagemOriginal = imagemOriginal;

		this.setDimensoesOriginais(this.imagemOriginal);

		this.imagemRedimensionada = this.redimensionarImagem(this.calcularProporcaoDaImagem());

		this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemRedimensionada);

		this.canetaDeDesenho.drawImage(this.imagemOriginal, 0, 0, this.larguraRedimensionada, this.alturaRedimensionada,
				new Canvas());

		this.encerrarCanetaDeDesenho();

		this.setDimensoesDefinitivas();

		this.imagemDefinitiva = new BufferedImage(this.larguraDefinitiva, this.alturaDefinitiva,
				BufferedImage.TRANSLUCENT);

		this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

		this.canetaDeDesenho.drawImage(this.imagemRedimensionada, 0,
				(int) ((this.alturaDefinitiva - this.alturaRedimensionada) * 0.65), new Canvas());

		this.encerrarCanetaDeDesenho();

		this.setFonteDeTextoDasFrases();

		if (conteudo.avaliacao() >= 8) {

			this.classificacao = Classificacao.ESPETACULAR;

			this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

			this.canetaDeDesenho.setColor(this.classificacao.getCor());

			this.canetaDeDesenho.setFont(this.fonteAvaliacao);

			this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseAvaliacao));

			this.calcularEixoY(0.06);

			this.canetaDeDesenho.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

			this.desenharContornoNoTexto(this.fraseAvaliacao);

			this.encerrarCanetaDeDesenho();

			this.setNotaEmFormatoSimbolo(conteudo.avaliacao());

			this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

			this.canetaDeDesenho.setColor(this.classificacao.getCor());

			this.canetaDeDesenho.setFont(this.fonteEstrelas);

			this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseEstrelas.toString()));

			this.calcularEixoY(0.18);

			this.canetaDeDesenho.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

			this.desenharContornoNoTexto(this.fraseEstrelas.toString());

			this.encerrarCanetaDeDesenho();

			this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

			this.canetaDeDesenho.setColor(this.classificacao.getCor());

			this.canetaDeDesenho.setFont(this.fonteClassificacao);

			this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.classificacao.getString()));

			this.calcularEixoY(0.995);

			this.canetaDeDesenho.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

			this.desenharContornoNoTexto(this.classificacao.getString());

			this.encerrarCanetaDeDesenho();

		} else {

			if (conteudo.avaliacao() < 8 && conteudo.avaliacao() > 5) {

				this.classificacao = Classificacao.BOM;

				this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

				this.canetaDeDesenho.setColor(this.classificacao.getCor());

				this.canetaDeDesenho.setFont(this.fonteAvaliacao);

				this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseAvaliacao));

				this.calcularEixoY(0.06);

				this.canetaDeDesenho.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

				this.desenharContornoNoTexto(this.fraseAvaliacao);

				this.encerrarCanetaDeDesenho();

				this.setNotaEmFormatoSimbolo(conteudo.avaliacao());

				this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

				this.canetaDeDesenho.setColor(this.classificacao.getCor());

				this.canetaDeDesenho.setFont(this.fonteEstrelas);

				this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseEstrelas.toString()));

				this.calcularEixoY(0.18);

				this.canetaDeDesenho.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

				this.desenharContornoNoTexto(this.fraseEstrelas.toString());

				this.encerrarCanetaDeDesenho();

				this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

				this.canetaDeDesenho.setColor(this.classificacao.getCor());

				this.canetaDeDesenho.setFont(this.fonteClassificacao);

				this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.classificacao.getString()));

				this.calcularEixoY(0.995);

				this.canetaDeDesenho.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

				this.desenharContornoNoTexto(this.classificacao.getString());

				this.encerrarCanetaDeDesenho();

			} else {

				if (conteudo.avaliacao() == 5) {

					this.classificacao = Classificacao.MEDIANO;

					this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

					this.canetaDeDesenho.setColor(this.classificacao.getCor());

					this.canetaDeDesenho.setFont(this.fonteAvaliacao);

					this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseAvaliacao));

					this.calcularEixoY(0.06);

					this.canetaDeDesenho.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

					this.desenharContornoNoTexto(this.fraseAvaliacao);

					this.encerrarCanetaDeDesenho();

					this.setNotaEmFormatoSimbolo(conteudo.avaliacao());

					this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

					this.canetaDeDesenho.setColor(this.classificacao.getCor());

					this.canetaDeDesenho.setFont(this.fonteEstrelas);

					this.calcularEixoX(
							this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseEstrelas.toString()));

					this.calcularEixoY(0.18);

					this.canetaDeDesenho.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

					this.desenharContornoNoTexto(this.fraseEstrelas.toString());

					this.encerrarCanetaDeDesenho();

					this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

					this.canetaDeDesenho.setColor(this.classificacao.getCor());

					this.canetaDeDesenho.setFont(this.fonteClassificacao);

					this.calcularEixoX(
							this.canetaDeDesenho.getFontMetrics().stringWidth(this.classificacao.getString()));

					this.calcularEixoY(0.995);

					this.canetaDeDesenho.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

					this.desenharContornoNoTexto(this.classificacao.getString());

					this.encerrarCanetaDeDesenho();

				} else {

					if (conteudo.avaliacao() > 0) {

						this.classificacao = Classificacao.RUIM;

						this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

						this.canetaDeDesenho.setColor(this.classificacao.getCor());

						this.canetaDeDesenho.setFont(this.fonteAvaliacao);

						this.calcularEixoX(this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseAvaliacao));

						this.calcularEixoY(0.06);

						this.canetaDeDesenho.drawString(this.fraseAvaliacao, this.eixoX, this.eixoY);

						this.desenharContornoNoTexto(this.fraseAvaliacao);

						this.encerrarCanetaDeDesenho();

						this.setNotaEmFormatoSimbolo(conteudo.avaliacao());

						this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

						this.canetaDeDesenho.setColor(this.classificacao.getCor());

						this.canetaDeDesenho.setFont(this.fonteEstrelas);

						this.calcularEixoX(
								this.canetaDeDesenho.getFontMetrics().stringWidth(this.fraseEstrelas.toString()));

						this.calcularEixoY(0.18);

						this.canetaDeDesenho.drawString(this.fraseEstrelas.toString(), this.eixoX, this.eixoY);

						this.desenharContornoNoTexto(this.fraseEstrelas.toString());

						this.encerrarCanetaDeDesenho();

						this.canetaDeDesenho = this.getCanetaDeDesenho(this.imagemDefinitiva);

						this.canetaDeDesenho.setColor(this.classificacao.getCor());

						this.canetaDeDesenho.setFont(this.fonteClassificacao);

						this.calcularEixoX(
								this.canetaDeDesenho.getFontMetrics().stringWidth(this.classificacao.getString()));

						this.calcularEixoY(0.995);

						this.canetaDeDesenho.drawString(this.classificacao.getString(), this.eixoX, this.eixoY);

						this.desenharContornoNoTexto(this.classificacao.getString());

						this.encerrarCanetaDeDesenho();

					} else {

						return this.imagemDefinitiva;

					}

				}

			}

		}

		return this.imagemDefinitiva;

	}

}
