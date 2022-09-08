package br.com.imersao.java.apresentador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.enumeration.SimboloUnicode;
import br.com.imersao.java.ui.componente.InterfaceDeTexto;

public class ApresentadorIMDB implements ApresentadorDeConteudo {

	private InterfaceDeTexto controleDeTexto;

	public ApresentadorIMDB(InterfaceDeTexto controleDeTexto) {

		this.controleDeTexto = controleDeTexto;

	}

	@Override
	public void apresentar(List<Conteudo> listaDeConteudos) {

		Conteudo conteudo = null;

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

		for (int i = 0; i < listaDeConteudos.size(); i++) {

			conteudo = listaDeConteudos.get(i);

			int avaliacao = new BigDecimal(conteudo.avaliacao()).setScale(1, RoundingMode.HALF_EVEN).intValue();

			this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
					+ EscapeANSI.FUNDO_BRANCO.getEscapeCaracter() + EscapeANSI.FONTE_PRETA.getEscapeCaracter() + " "
					+ (i + 1) + " " + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeTexto.exibirQuebraDeLinha();

			this.controleDeTexto.exibirTexto(
					EscapeANSI.FUNDO_VERMELHO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter() + "Titulo:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + " " + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ conteudo.titulo() + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeTexto.exibirQuebraDeLinha();

			this.controleDeTexto.exibirTexto(
					EscapeANSI.FUNDO_VERDE.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter() + "Descrição:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + " " + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ conteudo.descricao() + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeTexto.exibirQuebraDeLinha();

			this.controleDeTexto
					.exibirTexto(EscapeANSI.FUNDO_AZUL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Capa:" + EscapeANSI.NORMALIZADO.getEscapeCaracter() + " " + conteudo.uriImagem());

			this.controleDeTexto.exibirQuebraDeLinha();

			this.controleDeTexto
					.exibirTexto(EscapeANSI.FUNDO_AMARELO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Avaliação:" + EscapeANSI.NORMALIZADO.getEscapeCaracter() + " ");

			if (avaliacao > 0) {

				for (int x = 0; x < avaliacao; x++) {

					this.controleDeTexto.exibirTexto(SimboloUnicode.ESTRELA_BRILHANTE.getSimbolo());

				}

			} else {

				this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter() + "Não avaliado."
						+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

			}

			this.controleDeTexto.exibirQuebraDuplaDeLinha();

		}

	}

}
