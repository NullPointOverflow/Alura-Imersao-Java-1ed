package br.com.imersao.java.factory.sticker.handler.classificao;

import java.awt.image.BufferedImage;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.factory.sticker.componente.fonte.FonteFactory;

public interface ClassificacaoHandler {

	public void manipular(FonteFactory geradorDeFonte, BufferedImage imagem, Conteudo conteudo, int colocacao);

}
