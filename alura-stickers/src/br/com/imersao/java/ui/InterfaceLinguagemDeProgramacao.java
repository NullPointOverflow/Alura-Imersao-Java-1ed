package br.com.imersao.java.ui;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.com.imersao.java.apresentador.ApresentadorDeConteudo;
import br.com.imersao.java.entidade.Conteudo;
import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.enumeration.SimboloUnicode;
import br.com.imersao.java.enumeration.api.APIEnum;
import br.com.imersao.java.enumeration.api.LinguagemDeProgramacaoAPI;
import br.com.imersao.java.extrator.ExtratorDeConteudo;
import br.com.imersao.java.factory.ApresentadorFactory;
import br.com.imersao.java.factory.ExtratorFactory;
import br.com.imersao.java.factory.StickerAbstractFactory;
import br.com.imersao.java.factory.sticker.StickerFactory;
import br.com.imersao.java.ui.componente.InterfaceDeAviso;
import br.com.imersao.java.ui.componente.InterfaceDeMenu;
import br.com.imersao.java.ui.componente.InterfaceDeTexto;
import br.com.imersao.java.util.Default;
import br.com.imersao.java.util.WebClient;

public class InterfaceLinguagemDeProgramacao extends InterfaceDeUsuario {

	private Boolean interfaceAtiva;
	private InterfaceDeMenu controleDeMenu;
	private InterfaceDeTexto controleDeTexto;
	private InterfaceDeAviso controleDeAviso;
	private APIEnum linguagem;
	private WebClient clienteHTTP;
	private StickerFactory geradorDeFigurinhas;
	private String opcoeSelecionada = Default.getEmptyString();
	private String conteudoDaResposta = Default.getEmptyString();
	private ExtratorDeConteudo extrator;
	private List<Conteudo> listaDeConteudos = Default.getEmptyList();
	private ApresentadorDeConteudo apresentador;

	public InterfaceLinguagemDeProgramacao(InterfaceDeMenu controleDeMenu) {

		this.controleDeMenu = controleDeMenu;

		this.controleDeTexto = this.controleDeMenu.getInterfaceDeTexto();

		this.controleDeAviso = this.controleDeMenu.getInterfaceDeAvisos();

		this.clienteHTTP = new WebClient();

	}

	private void getMenuDeResultado(Scanner entrada) {

		Boolean opcaoResultadoAtivo = Boolean.TRUE;

		while (opcaoResultadoAtivo) {

			this.controleDeMenu
					.definirEnunciado(EscapeANSI.NEGRITO.getEscapeCaracter() + "Opções disponíveis para a lista:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "gerar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para gerar figurinhas de whatsapp utilizando o(s) resultado(s) retornado(s)."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "votar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para informar qual é a sua linguagem de programação favorita."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "voltar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu anterior." + EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.exibirMenu();

			this.controleDeMenu.exibirIndicacaoParaDigitacao();

			this.opcoeSelecionada = entrada.nextLine();

			switch (this.opcoeSelecionada) {

			case "gerar": {

				this.geradorDeFigurinhas = StickerAbstractFactory.gerar(this.linguagem.getGeradorDeStickers());

				this.controleDeAviso.exibirAvisoDeInicioGeracaoDeStickers();

				Map<String, Integer> statusDeGeracao = this.geradorDeFigurinhas.gerar(this.listaDeConteudos);

				this.controleDeAviso.exibirAvisoDeTerminoGeracaoDeStickers(statusDeGeracao.get("sucessos"),
						statusDeGeracao.get("falhas"));

				this.controleDeAviso.exibirAvisoDeRedirecionamento();

				opcaoResultadoAtivo = Boolean.FALSE;

				break;

			}

			case "votar": {

				Boolean votacaoAtiva = Boolean.TRUE;

				while (votacaoAtiva) {

					this.controleDeTexto.exibirQuebraDeLinha();

					this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Digite um número correspondende ao da lista de linguagens para atribuir sua preferência:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

					this.controleDeTexto.exibirQuebraDeLinha();

					this.controleDeMenu.exibirIndicacaoParaDigitacao();

					this.opcoeSelecionada = entrada.nextLine();

					try {

						Boolean insercaoDePreferenciaAtiva = Boolean.TRUE;
						String nomeDaLinguagemSelecionado;
						int numeroDaLinguagem;

						numeroDaLinguagem = Integer.parseInt(this.opcoeSelecionada) - 1;

						if (numeroDaLinguagem < 0 || numeroDaLinguagem >= this.listaDeConteudos.size()) {

							throw new IllegalArgumentException();

						}

						nomeDaLinguagemSelecionado = this.listaDeConteudos.get(numeroDaLinguagem).titulo();

						while (insercaoDePreferenciaAtiva) {

							this.controleDeTexto.exibirQuebraDeLinha();

							this.controleDeMenu
									.definirEnunciado(EscapeANSI.NEGRITO.getEscapeCaracter()
											+ "Você confirma sua preferência por utilzar a linguagem " + "\""
											+ EscapeANSI.ITALICO.getEscapeCaracter()
											+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
											+ nomeDaLinguagemSelecionado + EscapeANSI.NORMALIZADO.getEscapeCaracter()
											+ EscapeANSI.NEGRITO.getEscapeCaracter() + "\"?"
											+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
									.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
											+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
											+ EscapeANSI.ITALICO.getEscapeCaracter() + "sim"
											+ EscapeANSI.NORMALIZADO.getEscapeCaracter()
											+ EscapeANSI.NEGRITO.getEscapeCaracter() + "] para confirmar."
											+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
									.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
											+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
											+ EscapeANSI.ITALICO.getEscapeCaracter() + "não"
											+ EscapeANSI.NORMALIZADO.getEscapeCaracter()
											+ EscapeANSI.NEGRITO.getEscapeCaracter()
											+ "] para escolher outra linguagem."
											+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
									.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
											+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter()
											+ EscapeANSI.ITALICO.getEscapeCaracter() + "voltar"
											+ EscapeANSI.NORMALIZADO.getEscapeCaracter()
											+ EscapeANSI.NEGRITO.getEscapeCaracter()
											+ "] para voltar ao menu de opções."
											+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
									.exibirMenu();

							this.controleDeMenu.exibirIndicacaoParaDigitacao();

							this.opcoeSelecionada = entrada.nextLine();

							switch (this.opcoeSelecionada) {

							case "sim": {

								this.linguagem = LinguagemDeProgramacaoAPI.ATUALIZACAO_USUARIOS;

								this.extrator = ExtratorFactory.gerar(this.linguagem.getExtratorDeConteudo());

								Conteudo conteudo = new Conteudo(this.listaDeConteudos.get(numeroDaLinguagem).titulo(),
										this.listaDeConteudos.get(numeroDaLinguagem).avaliacao() + 1);

								String conteudoJson = this.extrator.extrair(conteudo);

								this.clienteHTTP.enviarDados(
										this.linguagem.getUrlDeConexao() + conteudo.titulo() + "/atualizar-utilizacao",
										conteudoJson);

								this.controleDeTexto.exibirQuebraDuplaDeLinha();

								this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
										+ "Sua preferência pela linguagem de programação \""
										+ EscapeANSI.ITALICO.getEscapeCaracter()
										+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
										+ nomeDaLinguagemSelecionado + EscapeANSI.NORMALIZADO.getEscapeCaracter()
										+ EscapeANSI.NEGRITO.getEscapeCaracter() + "\" foi computada com sucesso."
										+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

								this.controleDeTexto.exibirQuebraDuplaDeLinha();

								this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
										+ "Agradeçemos a participação. " + EscapeANSI.NORMALIZADO.getEscapeCaracter()
										+ SimboloUnicode.ROSTO.getSimbolo() + SimboloUnicode.POSITIVO.getSimbolo());

								this.controleDeTexto.exibirQuebraDeLinha();

								insercaoDePreferenciaAtiva = Boolean.FALSE;

								votacaoAtiva = Boolean.FALSE;

								opcaoResultadoAtivo = Boolean.FALSE;

								this.controleDeAviso.exibirAvisoDeRedirecionamento();

								break;

							}

							case "não": {

								insercaoDePreferenciaAtiva = Boolean.FALSE;

								break;

							}

							case "voltar": {

								insercaoDePreferenciaAtiva = Boolean.FALSE;

								votacaoAtiva = Boolean.FALSE;

								break;

							}

							default:

								this.controleDeAviso.exibirAvisoDeOpcaoInvalida();

								break;

							}

						}

					} catch (IllegalArgumentException e) {

						this.controleDeAviso.exibirAvisoDeOpcaoInvalida();

					}

				}

				break;

			}

			case "voltar": {

				opcaoResultadoAtivo = Boolean.FALSE;

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
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "rank"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver as linguagens de programação mais utilizadas no momento."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "voltar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu principal." + EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.exibirMenu();

			this.controleDeMenu.exibirIndicacaoParaDigitacao();

			this.opcoeSelecionada = entrada.nextLine();

			switch (this.opcoeSelecionada) {

			case "rank": {

				this.controleDeAviso.exibirAvisoDeEspera();

				this.linguagem = LinguagemDeProgramacaoAPI.RANK;

				this.extrator = ExtratorFactory.gerar(this.linguagem.getExtratorDeConteudo());

				this.conteudoDaResposta = this.clienteHTTP.buscarDados(this.linguagem.getUrlDeConexao());

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				if (this.listaDeConteudos.isEmpty()) {

					this.controleDeAviso.exibirAvisoSemConteudoParaApresentar();

				} else {

					this.apresentador = ApresentadorFactory.gerar(this.linguagem.getApresentadorDeConteudo(),
							this.controleDeTexto);

					this.controleDeTexto.exibirQuebraTriplaDeLinha();

					this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter() + "Lista:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

					this.apresentador.apresentar(listaDeConteudos);

					this.getMenuDeResultado(entrada);

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
