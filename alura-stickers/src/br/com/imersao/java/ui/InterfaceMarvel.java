package br.com.imersao.java.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import br.com.imersao.java.apresentador.ApresentadorDeConteudo;
import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.enumeration.api.APIEnum;
import br.com.imersao.java.enumeration.api.MarvelAPI;
import br.com.imersao.java.extrator.ExtratorDeConteudo;
import br.com.imersao.java.factory.ApresentadorFactory;
import br.com.imersao.java.factory.ExtratorFactory;
import br.com.imersao.java.factory.HashFactory;
import br.com.imersao.java.factory.sticker.StickerFactory;
import br.com.imersao.java.record.Conteudo;
import br.com.imersao.java.ui.componente.InterfaceDeAviso;
import br.com.imersao.java.ui.componente.InterfaceDeMenu;
import br.com.imersao.java.ui.componente.InterfaceDeTexto;
import br.com.imersao.java.util.Default;
import br.com.imersao.java.util.WebClient;

public class InterfaceMarvel extends InterfaceDeUsuario {

	private Boolean interfaceAtiva;
	private Properties chaveAPI;
	private HashFactory geradorDeHash;
	private InterfaceDeMenu controleDeMenu;
	private InterfaceDeTexto controleDeTexto;
	private InterfaceDeAviso controleDeAviso;
	private APIEnum marvel;
	private WebClient clienteHTTP;
	private StickerFactory geradorDeFigurinhas;
	private String opcoeSelecionada = Default.getEmptyString();
	private String conteudoDaResposta = Default.getEmptyString();
	private ExtratorDeConteudo extrator;
	private List<Conteudo> listaDeConteudos = Default.getEmptyList();
	private ApresentadorDeConteudo apresentador;

	public InterfaceMarvel(Properties chaveAPI, InterfaceDeMenu controleDeMenu) {

		this.chaveAPI = chaveAPI;

		this.geradorDeHash = new HashFactory("MD5");

		this.controleDeMenu = controleDeMenu;

		this.controleDeTexto = this.controleDeMenu.getInterfaceDeTexto();

		this.controleDeAviso = this.controleDeMenu.getInterfaceDeAvisos();

		this.clienteHTTP = new WebClient();

		this.geradorDeFigurinhas = new StickerFactory();

	}

	private void getMenuDeStickers(Scanner entrada) {

		Boolean opcaoStickerAtivo = Boolean.TRUE;

		while (opcaoStickerAtivo) {

			this.controleDeMenu.exibirMenuDeStickers();

			this.controleDeMenu.exibirIndicacaoParaDigitacao();

			this.opcoeSelecionada = entrada.nextLine();

			switch (this.opcoeSelecionada) {

			case "sim": {

				int sucessos = 0;
				int falhas = 0;

				this.controleDeAviso.exibirAvisoDeInicioGeracaoDeStickers();

				for (Conteudo conteudo : this.listaDeConteudos) {

					try (InputStream fluxoDeDados = new URL(conteudo.uriImagem()).openStream()) {

						this.geradorDeFigurinhas.gerar(fluxoDeDados, conteudo);

						sucessos++;

					} catch (IOException e) {

						falhas++;

					}

				}

				this.controleDeAviso.exibirAvisoDeTerminoGeracaoDeStickers(sucessos, falhas);

				opcaoStickerAtivo = Boolean.FALSE;

				break;

			}

			case "não": {

				opcaoStickerAtivo = Boolean.FALSE;

				break;

			}

			default:

				this.controleDeAviso.exibirAvisoDeOpcaoInvalida();

				break;

			}

		}

	}

	@Override
	public void iniciar(Scanner entrada) {

		this.interfaceAtiva = Boolean.TRUE;

		while (interfaceAtiva) {

			this.controleDeMenu
					.definirEnunciado(EscapeANSI.NEGRITO.getEscapeCaracter() + "Opções de conteúdo disponíveis:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "herois"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver relação de personagens da Marvel de forma alfabética."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "voltar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu principal." + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeMenu.exibirMenu();

			this.controleDeMenu.exibirIndicacaoParaDigitacao();

			this.opcoeSelecionada = entrada.nextLine();

			switch (this.opcoeSelecionada) {

			case "herois": {

				this.marvel = MarvelAPI.PERSONAGENS;

				this.extrator = ExtratorFactory.gerar(this.marvel.getExtratorDeConteudo());

				this.apresentador = ApresentadorFactory.gerar(this.marvel.getApresentadorDeConteudo(),
						this.controleDeTexto);

				Boolean menuHeroisAtivo = Boolean.TRUE;

				String conteudoBuscado = Default.getEmptyString();

				while (menuHeroisAtivo) {

					this.controleDeTexto.exibirQuebraDeLinha();

					this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Digite a letra ou o número com qual o nome os personagens começam:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

					this.controleDeTexto.exibirQuebraDeLinha();

					this.controleDeMenu.exibirIndicacaoParaDigitacao();

					conteudoBuscado = entrada.nextLine();

					try {

						if (conteudoBuscado.length() > 1) {

							throw new IllegalArgumentException();

						}

						this.controleDeAviso.exibirAvisoDeEspera();

						String timestamp = Long.toString(System.currentTimeMillis());

						String chavePublica = this.chaveAPI.getProperty("marvel-public-api-key");

						String hash = this.geradorDeHash
								.gerar(timestamp + this.chaveAPI.getProperty("marvel-private-api-key") + chavePublica);

						this.conteudoDaResposta = this.clienteHTTP
								.buscarDados(this.marvel.getUrlDeConexao() + "&nameStartsWith=" + conteudoBuscado
										+ "&ts=" + timestamp + "&apikey=" + chavePublica + "&hash=" + hash);

						this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

						if (listaDeConteudos.size() == 0) {

							this.controleDeAviso.exibirAvisoDeBuscaSemResultados(conteudoBuscado);

							menuHeroisAtivo = Boolean.FALSE;

						} else {

							this.controleDeAviso.exibirAvisoDeBuscaComResultados(conteudoBuscado);

							this.apresentador.apresentar(listaDeConteudos);

							this.getMenuDeStickers(entrada);

							this.controleDeAviso.exibirAvisoDeRedirecionamento();

							menuHeroisAtivo = Boolean.FALSE;

						}

					} catch (IllegalArgumentException e) {

						this.controleDeAviso.exibirAvisoCustomizado(
								"Digite apenas uma única letra (maiúscula ou minúscula) ou um único número que seja o ínicio do nome dos personagens buscados.");

					}

				}

				break;

			}

			case "voltar": {

				this.interfaceAtiva = Boolean.FALSE;

				this.controleDeAviso.exibirAvisoDeRedirecionamento();

				break;

			}

			default:

				this.controleDeAviso.exibirAvisoDeOpcaoInvalida();

				break;

			}

		}

	}

}
