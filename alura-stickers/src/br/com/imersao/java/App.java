package br.com.imersao.java;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.enumeration.SimboloUnicode;
import br.com.imersao.java.exception.ArquivoDeConfiguracaoException;
import br.com.imersao.java.ui.InterfaceDeUsuario;
import br.com.imersao.java.ui.InterfaceIMDB;
import br.com.imersao.java.ui.InterfaceLinguagemDeProgramacao;
import br.com.imersao.java.ui.InterfaceMarvel;
import br.com.imersao.java.ui.InterfaceNASA;
import br.com.imersao.java.ui.componente.InterfaceDeAviso;
import br.com.imersao.java.ui.componente.InterfaceDeMenu;
import br.com.imersao.java.ui.componente.InterfaceDeTexto;
import br.com.imersao.java.util.Default;

public class App {

	private static final Properties PROPRIEDADES = new Properties();

	public static void init() {

		try (InputStream leitorDeArquivo = App.class.getClassLoader()
				.getResourceAsStream("properties/applicacao.properties")) {

			PROPRIEDADES.load(leitorDeArquivo);

		} catch (Exception e) {

			throw new ArquivoDeConfiguracaoException("Não foi possível localizar o arquivo de configuração informado.",
					e);

		}

	}

	public static void main(String[] args) throws Exception {

		Boolean programaAtivo = Boolean.TRUE;
		InterfaceDeMenu controleDeMenu = new InterfaceDeMenu();
		InterfaceDeTexto controleDeTexto = controleDeMenu.getInterfaceDeTexto();
		InterfaceDeAviso contoleDeAviso = controleDeMenu.getInterfaceDeAvisos();
		InterfaceDeUsuario imdb = new InterfaceIMDB(PROPRIEDADES, controleDeMenu);
		InterfaceDeUsuario nasa = new InterfaceNASA(PROPRIEDADES, controleDeMenu);
		InterfaceDeUsuario marvel = new InterfaceMarvel(PROPRIEDADES, controleDeMenu);
		InterfaceDeUsuario linguagem = new InterfaceLinguagemDeProgramacao(controleDeMenu);
		String opcoeSelecionada = Default.getEmptyString();

		try (Scanner entrada = new Scanner(System.in)) {

			init();

			controleDeTexto.exibirTexto(SimboloUnicode.BANDEIRA_LARGADA.getSimbolo()
					+ EscapeANSI.NEGRITO.getEscapeCaracter() + " BEM VINDO! "
					+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.BANDEIRA_LARGADA.getSimbolo());

			controleDeTexto.exibirQuebraDuplaDeLinha();

			while (programaAtivo) {

				controleDeMenu
						.definirEnunciado(EscapeANSI.NEGRITO.getEscapeCaracter() + "O que gostaria de acessar?"
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
						.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
								+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + "imdb"
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
								+ "] para ter acesso a maior base de dados publica sobre filmes e séries da internet."
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
						.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
								+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + "nasa"
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
								+ "] para acessar as fotos que registram nosso universo."
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
						.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
								+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + "marvel"
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
								+ "] para acessar a maior base de dados publica sobre personagens dos quadrinhos."
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
						.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
								+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + "linguagem"
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
								+ "] para acessar a classificação de linguagens de programação mais usadas no momento."
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
						.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
								+ EscapeANSI.FONTE_MAGENTA_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + "sair"
								+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
								+ "] para encerrar o programa." + EscapeANSI.NORMALIZADO.getEscapeCaracter())
						.exibirMenu();

				controleDeMenu.exibirIndicacaoParaDigitacao();

				opcoeSelecionada = entrada.nextLine();

				switch (opcoeSelecionada) {

				case "imdb": {

					imdb.iniciar(entrada);

					break;

				}

				case "nasa": {

					nasa.iniciar(entrada);

					break;

				}

				case "marvel": {

					marvel.iniciar(entrada);

					break;

				}

				case "linguagem": {

					linguagem.iniciar(entrada);

					break;

				}

				case "sair": {

					programaAtivo = false;

					controleDeTexto.exibirQuebraDuplaDeLinha();

					controleDeTexto.exibirTexto(SimboloUnicode.ACENO.getSimbolo()
							+ EscapeANSI.NEGRITO.getEscapeCaracter() + " Programa encerrado. Até mais! "
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.ACENO.getSimbolo());

					controleDeTexto.exibirQuebraDuplaDeLinha();

					break;
				}

				default:

					contoleDeAviso.exibirAvisoDeOpcaoInvalida();

					break;

				}

			}

		} catch (Exception e) {

			controleDeTexto.exibirQuebraTriplaDeLinha();

			controleDeTexto.exibirTexto(SimboloUnicode.AVISO.getSimbolo() + EscapeANSI.NEGRITO.getEscapeCaracter()
					+ " Programa encerrado devido a problemas internos. Por favor tente novamente mais tarde. "
					+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + SimboloUnicode.AVISO.getSimbolo());

			e.printStackTrace();

		}

	}

}
