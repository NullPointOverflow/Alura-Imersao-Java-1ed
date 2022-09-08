package br.com.imersao.java.ui.componente;

import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.enumeration.SimboloUnicode;

public class InterfaceDeAviso {

	private InterfaceDeTexto controleDeTexto;

	public InterfaceDeAviso(InterfaceDeTexto controleDeTexto) {

		this.controleDeTexto = controleDeTexto;

	}

	public void exibirAvisoDeOpcaoInvalida() {

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

		this.controleDeTexto.exibirTexto(SimboloUnicode.PROIBIDO.getSimbolo() + EscapeANSI.NEGRITO.getEscapeCaracter()
				+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + " Opção inválida. "
				+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.PROIBIDO.getSimbolo());

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

	}

	public void exibirAvisoCustomizado(String aviso) {

		this.controleDeTexto.exibirTexto(SimboloUnicode.AVISO.getSimbolo() + EscapeANSI.NEGRITO.getEscapeCaracter()
				+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + " " + aviso + " "
				+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.AVISO.getSimbolo());

	}

	public void exibirAvisoDeRedirecionamento() {

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

		this.controleDeTexto.exibirTexto(
				EscapeANSI.NEGRITO.getEscapeCaracter() + EscapeANSI.ITALICO.getEscapeCaracter() + "Redirecionando..."
						+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.ATUALIZACAO.getSimbolo());

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

	}

	public void exibirAvisoDeEspera() {

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

		this.controleDeTexto.exibirTexto(SimboloUnicode.PARADA.getSimbolo() + EscapeANSI.ITALICO.getEscapeCaracter()
				+ EscapeANSI.NEGRITO.getEscapeCaracter() + " Aguarde alguns instantes. "
				+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.PARADA.getSimbolo());

	}

	public void exibirAvisoDeInicioGeracaoDeStickers() {

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

		this.controleDeTexto.exibirTexto(SimboloUnicode.CONSTRUCAO.getSimbolo() + EscapeANSI.NEGRITO.getEscapeCaracter()
				+ EscapeANSI.ITALICO.getEscapeCaracter() + " Gerando figurinhas. Aguarde, por favor. "
				+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.CONSTRUCAO.getSimbolo());

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

	}

	public void exibirAvisoDeTerminoGeracaoDeStickers(int sucessos, int falhas) {

		this.controleDeTexto.exibirTexto(SimboloUnicode.CONSTRUCAO.getSimbolo() + EscapeANSI.ITALICO.getEscapeCaracter()
				+ EscapeANSI.NEGRITO.getEscapeCaracter() + " Geração de figurinhas terminada: "
				+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.CONSTRUCAO.getSimbolo());
		this.controleDeTexto.exibirQuebraDuplaDeLinha();
		this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter() + "- "
				+ SimboloUnicode.APROVADO.getSimbolo() + " " + EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
				+ EscapeANSI.ITALICO.getEscapeCaracter() + sucessos + EscapeANSI.NORMALIZADO.getEscapeCaracter()
				+ EscapeANSI.ITALICO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
				+ " figurinha(s) gerada(s) com sucesso." + EscapeANSI.NORMALIZADO.getEscapeCaracter());
		this.controleDeTexto.exibirQuebraDeLinha();
		this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter() + "- "
				+ SimboloUnicode.REPROVADO.getSimbolo() + " " + EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
				+ EscapeANSI.ITALICO.getEscapeCaracter() + falhas + EscapeANSI.NORMALIZADO.getEscapeCaracter()
				+ EscapeANSI.ITALICO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
				+ " figurinhas não puderam ser geradas." + EscapeANSI.NORMALIZADO.getEscapeCaracter());

		this.controleDeTexto.exibirQuebraDeLinha();

	}

	public void exibirAvisoDeBuscaSemResultados(String termo) {

		this.controleDeTexto.exibirQuebraTriplaDeLinha();

		this.controleDeTexto.exibirTexto(SimboloUnicode.NEGATIVO.getSimbolo() + EscapeANSI.NEGRITO.getEscapeCaracter()
				+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
				+ " Nenhum resultado encontrado para o termo \"" + EscapeANSI.NORMALIZADO.getEscapeCaracter()
				+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter() + EscapeANSI.ITALICO.getEscapeCaracter()
				+ termo + EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
				+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + "\". "
				+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.NEGATIVO.getSimbolo());

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

	}

	public void exibirAvisoDeBuscaComResultados(String termo) {

		this.controleDeTexto.exibirQuebraTriplaDeLinha();

		this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter() + "Resultado(s) da busca por \""
				+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter() + EscapeANSI.ITALICO.getEscapeCaracter()
				+ termo + EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter() + "\":");

	}

}
