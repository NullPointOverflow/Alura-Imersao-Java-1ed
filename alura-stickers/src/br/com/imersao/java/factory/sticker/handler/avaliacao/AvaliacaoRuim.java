package br.com.imersao.java.factory.sticker.handler.avaliacao;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.enumeration.Avaliacao;
import br.com.imersao.java.factory.sticker.componente.CanetaFactory;
import br.com.imersao.java.factory.sticker.componente.ContornoFactory;
import br.com.imersao.java.factory.sticker.componente.fonte.FonteFactory;
import br.com.imersao.java.util.CalculadorDeImagem;

public class AvaliacaoRuim implements AvaliacaoHandler, CalculadorDeImagem {

	private final Avaliacao avaliacao;
	private CanetaFactory geradorDeCaneta;
	private FonteFactory geradorDeFonte;
	private ContornoFactory geradorDeContorno;
	private Graphics2D caneta;

	public AvaliacaoRuim() {

		this.avaliacao = Avaliacao.RUIM;

		this.geradorDeCaneta = new CanetaFactory();

		this.geradorDeContorno = new ContornoFactory();

	}

	private Boolean deveManipular(int nota) {

		if (nota > 0) {

			return Boolean.TRUE;

		}

		return Boolean.FALSE;

	}

	private void escreverCabecalho(BufferedImage imagem) {

		this.caneta = this.geradorDeCaneta.gerar(imagem);

		this.caneta.setColor(this.avaliacao.getCor());

		this.caneta.setFont(this.geradorDeFonte.gerarFonteDeCabecalho(imagem));

		String fraseCabecalho = "AVALIAÇÃO";

		int eixoX = this.calcularPosicaoNoEixoX(imagem, caneta.getFontMetrics().stringWidth(fraseCabecalho));

		int eixoY = this.calcularPosicaoNoEixoY(imagem, 0.072);

		this.caneta.drawString(fraseCabecalho, eixoX, eixoY);

		this.geradorDeContorno.gerar(imagem, this.caneta, fraseCabecalho, eixoX, eixoY);

		this.caneta.dispose();

	}

	private void escreverCentro(BufferedImage imagem, int nota) {

		this.caneta = this.geradorDeCaneta.gerar(imagem);

		this.caneta.setColor(this.avaliacao.getCor());

		this.caneta.setFont(this.geradorDeFonte.gerarFonteDeCentro(imagem));

		String estrelas = this.getNotaComoSimboloUnicode(nota);

		int eixoX = this.calcularPosicaoNoEixoX(imagem, caneta.getFontMetrics().stringWidth(estrelas));

		int eixoY = this.calcularPosicaoNoEixoY(imagem, 0.18);

		this.caneta.drawString(estrelas, eixoX, eixoY);

		this.geradorDeContorno.gerar(imagem, this.caneta, estrelas, eixoX, eixoY);

		this.caneta.dispose();

	}

	private void escreverRodape(BufferedImage imagem) {

		this.caneta = this.geradorDeCaneta.gerar(imagem);

		this.caneta.setColor(this.avaliacao.getCor());

		this.caneta.setFont(geradorDeFonte.gerarFonteDeRodape(imagem));

		int eixoX = this.calcularPosicaoNoEixoX(imagem,
				caneta.getFontMetrics().stringWidth(this.avaliacao.getString()));

		int eixoY = this.calcularPosicaoNoEixoY(imagem, 0.995);

		this.caneta.drawString(this.avaliacao.getString(), eixoX, eixoY);

		this.geradorDeContorno.gerar(imagem, this.caneta, this.avaliacao.getString(), eixoX, eixoY);

		this.caneta.dispose();

	}

	@Override
	public void manipular(FonteFactory geradorDeFonte, BufferedImage imagem, Conteudo conteudo) {

		if (!(this.deveManipular(conteudo.avaliacao()))) {

			return;

		}

		this.geradorDeFonte = geradorDeFonte;

		this.escreverCabecalho(imagem);

		this.escreverCentro(imagem, conteudo.avaliacao());

		this.escreverRodape(imagem);

		return;

	}

}
