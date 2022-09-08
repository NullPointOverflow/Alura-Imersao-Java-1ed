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
import br.com.imersao.java.enumeration.api.NasaAPI;
import br.com.imersao.java.extrator.ExtratorDeConteudo;
import br.com.imersao.java.factory.ApresentadorFactory;
import br.com.imersao.java.factory.ExtratorFactory;
import br.com.imersao.java.factory.sticker.StickerFactory;
import br.com.imersao.java.record.Conteudo;
import br.com.imersao.java.ui.componente.InterfaceDeAviso;
import br.com.imersao.java.ui.componente.InterfaceDeMenu;
import br.com.imersao.java.ui.componente.InterfaceDeTexto;
import br.com.imersao.java.util.Default;
import br.com.imersao.java.util.WebClient;

public class InterfaceNASA extends InterfaceDeUsuario {

	private Boolean interfaceAtiva;
	private Properties chaveAPI;
	private InterfaceDeMenu controleDeMenu;
	private InterfaceDeTexto controleDeTexto;
	private InterfaceDeAviso controleDeAviso;
	private APIEnum nasa;
	private WebClient clienteHTTP;
	private StickerFactory geradorDeFigurinhas;
	private String opcoeSelecionada = Default.getEmptyString();
	private String conteudoDaResposta = Default.getEmptyString();
	private ExtratorDeConteudo extrator;
	private List<Conteudo> listaDeConteudos = Default.getEmptyList();
	private ApresentadorDeConteudo apresentador;

	public InterfaceNASA(Properties chaveAPI, InterfaceDeMenu controleDeMenu) {

		this.chaveAPI = chaveAPI;

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

				this.controleDeAviso.exibirAvisoDeRedirecionamento();

				opcaoStickerAtivo = Boolean.FALSE;

				break;

			}

			case "não": {

				opcaoStickerAtivo = Boolean.FALSE;

				this.controleDeAviso.exibirAvisoDeRedirecionamento();

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
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "apod"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver a foto selecionada pela NASA como a \"Foto Astronômica do Dia\" ("
							+ EscapeANSI.ITALICO.getEscapeCaracter()
							+ EscapeANSI.FONTE_CIANO_BRILHANTE.getEscapeCaracter() + "Astronomy Picture of the Day"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter() + ")."
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

			case "apod": {

				this.controleDeAviso.exibirAvisoDeEspera();

				this.nasa = NasaAPI.APOD;

				this.extrator = ExtratorFactory.gerar(this.nasa.getExtratorDeConteudo());

				this.conteudoDaResposta = this.clienteHTTP
						.buscarDados(this.nasa.getUrlDeConexao() + this.chaveAPI.getProperty("nasa-api-key"));

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				this.apresentador = ApresentadorFactory.gerar(this.nasa.getApresentadorDeConteudo(),
						this.controleDeTexto);

				this.apresentador.apresentar(listaDeConteudos);

				this.getMenuDeStickers(entrada);

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
