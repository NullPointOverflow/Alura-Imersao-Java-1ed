package br.com.imersao.java.apresentador;

import java.util.List;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.ui.componente.InterfaceDeTexto;

public class ApresentadorLinguagemDeProgramacao implements ApresentadorDeConteudo {

	private InterfaceDeTexto controleDeTexto;

	public ApresentadorLinguagemDeProgramacao(InterfaceDeTexto controleDeTexto) {

		this.controleDeTexto = controleDeTexto;

	}

	@Override
	public void apresentar(List<Conteudo> listaDeConteudos) {

		Conteudo conteudo = null;

		this.controleDeTexto.exibirQuebraTriplaDeLinha();

		for (int i = 0; i < listaDeConteudos.size(); i++) {

			conteudo = listaDeConteudos.get(i);

			this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
					+ EscapeANSI.FUNDO_BRANCO.getEscapeCaracter() + EscapeANSI.FONTE_PRETA.getEscapeCaracter() + " "
					+ (i + 1) + " " + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeTexto.exibirQuebraDeLinha();

			this.controleDeTexto.exibirTexto(
					EscapeANSI.FUNDO_VERMELHO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter() + "Nome:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + " " + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ conteudo.titulo() + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeTexto.exibirQuebraDeLinha();

			this.controleDeTexto
					.exibirTexto(EscapeANSI.FUNDO_AZUL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Logotipo:" + EscapeANSI.NORMALIZADO.getEscapeCaracter() + " " + conteudo.uriImagem());

			this.controleDeTexto.exibirQuebraDeLinha();

			this.controleDeTexto
					.exibirTexto(EscapeANSI.FUNDO_VERDE.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Classificação:" + EscapeANSI.NORMALIZADO.getEscapeCaracter() + " "
							+ EscapeANSI.NEGRITO.getEscapeCaracter() + (i + 1) + "ª linguagem mais usada. ("
							+ conteudo.avaliacao() + " utilizadores)" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeTexto.exibirQuebraDuplaDeLinha();

		}

	}

}
