package br.com.imersao.java.factory.sticker.handler.avaliacao;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.enumeration.SimboloUnicode;
import br.com.imersao.java.factory.sticker.componente.fonte.FonteFactory;

public interface AvaliacaoHandler {

	default String getNotaComoSimboloUnicode(int avaliacao) {

		StringBuilder estrelas = new StringBuilder(10);

		IntStream.range(0, avaliacao).forEach(inteiro -> estrelas.append(SimboloUnicode.ESTRELA.getSimbolo()));

		return estrelas.toString();

	}

	public void manipular(FonteFactory geradorDeFonte, BufferedImage imagem, Conteudo conteudo);

}
