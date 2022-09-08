package br.com.imersao.java.factory.sticker.handler.classificao;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.enumeration.Classificacao;
import br.com.imersao.java.factory.sticker.componente.CanetaFactory;
import br.com.imersao.java.factory.sticker.componente.ContornoFactory;
import br.com.imersao.java.factory.sticker.componente.fonte.FonteFactory;
import br.com.imersao.java.util.CalculadorDeImagem;

public class ClassificacaoOutros implements ClassificacaoHandler, CalculadorDeImagem {

	private final Classificacao classificacao;
	private CanetaFactory geradorDeCaneta;
	private FonteFactory geradorDeFonte;
	private ContornoFactory geradorDeContorno;
	private Graphics2D caneta;

	public ClassificacaoOutros() {

		this.classificacao = Classificacao.OUTRAS_POSICOES;

		this.geradorDeCaneta = new CanetaFactory();

		this.geradorDeContorno = new ContornoFactory();

	}

	private Boolean deveManipular(int colocacao) {

		if (colocacao > 3) {

			return Boolean.TRUE;

		}

		return Boolean.FALSE;

	}

	private void escreverCabecalho(BufferedImage imagem, int colocacao) {

		String fraseInicio = String.format("%dº", colocacao);
		String fraseFim = String.format("Colocado", colocacao);

		this.caneta = this.geradorDeCaneta.gerar(imagem);

		this.caneta.setColor(this.classificacao.getCor());

		this.caneta.setFont(this.geradorDeFonte.gerarFonteDeCabecalho(imagem));

		int eixoX = this.calcularPosicaoNoEixoX(imagem, caneta.getFontMetrics().stringWidth(fraseInicio));

		int eixoY = this.calcularPosicaoNoEixoY(imagem, 0.08);

		this.caneta.drawString(fraseInicio, eixoX, eixoY);

		this.geradorDeContorno.gerar(imagem, this.caneta, fraseInicio, eixoX, eixoY);

		this.caneta.dispose();

		this.caneta = this.geradorDeCaneta.gerar(imagem);

		this.caneta.setColor(this.classificacao.getCor());

		this.caneta.setFont(this.geradorDeFonte.gerarFonteDeCabecalho(imagem));

		eixoX = this.calcularPosicaoNoEixoX(imagem, caneta.getFontMetrics().stringWidth(fraseFim));

		eixoY = eixoY + this.caneta.getFontMetrics().getHeight();

		this.caneta.drawString(fraseFim, eixoX, eixoY);

		this.geradorDeContorno.gerar(imagem, this.caneta, fraseFim, eixoX, eixoY);

		this.caneta.dispose();

	}

	private void escreverRodape(BufferedImage imagem) {

		String fraseInicio = "LINGUAGENS DE PROGRAMAÇÃO";
		String fraseFim = "MAIS UTILIZADAS";

		this.caneta = this.geradorDeCaneta.gerar(imagem);

		this.caneta.setColor(this.classificacao.getCor());

		this.caneta.setFont(this.geradorDeFonte.gerarFonteDeRodape(imagem));

		int eixoX = this.calcularPosicaoNoEixoX(imagem, caneta.getFontMetrics().stringWidth(fraseInicio));

		int eixoY = this.calcularPosicaoNoEixoY(imagem, 0.925);

		this.caneta.drawString(fraseInicio, eixoX, eixoY);

		this.geradorDeContorno.gerar(imagem, this.caneta, fraseInicio, eixoX, eixoY);

		this.caneta.dispose();

		this.caneta = this.geradorDeCaneta.gerar(imagem);

		this.caneta.setColor(this.classificacao.getCor());

		this.caneta.setFont(this.geradorDeFonte.gerarFonteDeRodape(imagem));

		eixoX = this.calcularPosicaoNoEixoX(imagem, caneta.getFontMetrics().stringWidth(fraseFim));

		eixoY = eixoY + this.caneta.getFontMetrics().getHeight();

		this.caneta.drawString(fraseFim, eixoX, eixoY);

		this.geradorDeContorno.gerar(imagem, this.caneta, fraseFim, eixoX, eixoY);

		this.caneta.dispose();

	}

	@Override
	public void manipular(FonteFactory geradorDeFonte, BufferedImage imagem, Conteudo conteudo, int colocacao) {

		if (!(this.deveManipular(colocacao))) {

			return;

		}

		this.geradorDeFonte = geradorDeFonte;

		this.escreverCabecalho(imagem, colocacao);

		this.escreverRodape(imagem);

		return;

	}

}