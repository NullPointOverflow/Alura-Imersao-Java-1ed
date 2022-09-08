package br.com.imersao.java.ui.componente;

import java.util.ArrayList;
import java.util.List;

import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.util.Default;

public class InterfaceDeMenu {

	private InterfaceDeTexto controleDeTexto;
	private InterfaceDeAviso controleDeAvisos;
	private String enunciado;
	private List<String> opcoes;

	public InterfaceDeMenu() {

		this.controleDeTexto = new InterfaceDeTexto();
		this.controleDeAvisos = new InterfaceDeAviso(this.controleDeTexto);
		this.opcoes = new ArrayList<String>(5);

	}

	public InterfaceDeTexto getInterfaceDeTexto() {

		return controleDeTexto;

	}

	public InterfaceDeAviso getInterfaceDeAvisos() {

		return controleDeAvisos;

	}

	public InterfaceDeMenu definirEnunciado(String enunciado) {

		this.enunciado = enunciado;

		return this;

	}

	public InterfaceDeMenu comOpcao(String opcao) {

		this.opcoes.add(opcao);

		return this;

	}

	public void exibirMenu() {

		this.controleDeTexto.exibirQuebraDeLinha();

		this.controleDeTexto.exibirTexto(this.enunciado);

		this.controleDeTexto.exibirQuebraDuplaDeLinha();

		for (String opcao : this.opcoes) {

			this.controleDeTexto.exibirTexto(opcao);

			this.controleDeTexto.exibirQuebraDeLinha();

		}

		this.enunciado = Default.getEmptyString();

		this.opcoes.removeAll(opcoes);

	}

	public void exibirIndicacaoParaDigitacao() {

		this.controleDeTexto.exibirTexto(
				EscapeANSI.PISCANTE.getEscapeCaracter() + "> " + EscapeANSI.NORMALIZADO.getEscapeCaracter());

	}

	public void exibirAvisoOpcaoInvalida() {

		this.controleDeAvisos.exibirAvisoDeOpcaoInvalida();

	}

	public void exibirAvisoCustomizado(String aviso) {

		this.controleDeAvisos.exibirAvisoCustomizado(aviso);

	}

	public void exibirMenuDeStickers() {

		this.definirEnunciado(EscapeANSI.NEGRITO.getEscapeCaracter()
				+ "Gostaria de gerar figurinhas de whatsapp utilizando a(s) imagem(s) listada(s)?"
				+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
				.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
						+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter() + EscapeANSI.ITALICO.getEscapeCaracter()
						+ "sim" + EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
						+ "] para gerar as figurinhas." + EscapeANSI.NORMALIZADO.getEscapeCaracter())
				.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
						+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
						+ EscapeANSI.ITALICO.getEscapeCaracter() + "não" + EscapeANSI.NORMALIZADO.getEscapeCaracter()
						+ EscapeANSI.NEGRITO.getEscapeCaracter() + "] para voltar ao menu de anterior."
						+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

		this.exibirMenu();

	}

}
