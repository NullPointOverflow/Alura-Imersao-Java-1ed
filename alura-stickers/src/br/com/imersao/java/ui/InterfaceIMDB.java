package br.com.imersao.java.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import br.com.imersao.java.apresentador.ApresentadorDeConteudo;
import br.com.imersao.java.enumeration.EscapeANSI;
import br.com.imersao.java.enumeration.SimboloUnicode;
import br.com.imersao.java.enumeration.api.APIEnum;
import br.com.imersao.java.enumeration.api.ImdbAPI;
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

public class InterfaceIMDB extends InterfaceDeUsuario {

	private Boolean interfaceAtiva;
	private Properties chaveAPI;
	private InterfaceDeMenu controleDeMenu;
	private InterfaceDeTexto controleDeTexto;
	private InterfaceDeAviso controleDeAviso;
	private Boolean menuDeConteudoAtivo;
	private APIEnum imdb;
	private WebClient clienteHTTP;
	private StickerFactory geradorDeFigurinhas;
	private String opcoeSelecionada = Default.getEmptyString();
	private String conteudoDaResposta = Default.getEmptyString();
	private ExtratorDeConteudo extrator;
	private List<Conteudo> listaDeConteudos = Default.getEmptyList();
	private ApresentadorDeConteudo apresentador;

	public InterfaceIMDB(Properties chaveAPI, InterfaceDeMenu controleDeMenu) {

		this.chaveAPI = chaveAPI;

		this.controleDeMenu = controleDeMenu;

		this.controleDeTexto = this.controleDeMenu.getInterfaceDeTexto();

		this.controleDeAviso = this.controleDeMenu.getInterfaceDeAvisos();

		this.clienteHTTP = new WebClient();

		this.geradorDeFigurinhas = new StickerFactory();

	}

	private void getMenuDeResultadoDeBusca(Scanner entrada) {

		Boolean opcaoResultadoDeBuscaAtivo = Boolean.TRUE;

		while (opcaoResultadoDeBuscaAtivo) {

			this.controleDeMenu
					.definirEnunciado(
							EscapeANSI.NEGRITO.getEscapeCaracter() + "Opções disponíveis para o resultado da busca:"
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
							+ "] para atribuir sua própria avaliação a um conteúdo."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "voltar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu de filmes e séries." + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeMenu.exibirMenu();

			this.controleDeMenu.exibirIndicacaoParaDigitacao();

			this.opcoeSelecionada = entrada.nextLine();

			switch (this.opcoeSelecionada) {

			case "gerar": {

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

				opcaoResultadoDeBuscaAtivo = Boolean.FALSE;

				break;

			}

			case "votar": {

				Boolean votacaoAtivada = Boolean.TRUE;

				while (votacaoAtivada) {

					this.controleDeTexto.exibirQuebraDeLinha();

					this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Digite o número correspondende ao resultado encontrado para atribuir sua avaliação:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

					this.controleDeTexto.exibirQuebraDeLinha();

					this.controleDeMenu.exibirIndicacaoParaDigitacao();

					this.opcoeSelecionada = entrada.nextLine();

					try {

						Boolean insercaoDeNotaAtivada = Boolean.TRUE;
						String tituloDoFilmeSelecionado;
						int numeroDoResultado;
						int avaliacao;

						numeroDoResultado = Integer.parseInt(this.opcoeSelecionada) - 1;

						if (numeroDoResultado < 0 || numeroDoResultado >= this.listaDeConteudos.size()) {

							throw new IllegalArgumentException();

						}

						tituloDoFilmeSelecionado = this.listaDeConteudos.get(numeroDoResultado).titulo();

						while (insercaoDeNotaAtivada) {

							this.controleDeTexto.exibirQuebraDeLinha();

							this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
									+ "Entre com uma avaliação (nota de 1 a 10) para " + "\""
									+ EscapeANSI.ITALICO.getEscapeCaracter()
									+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter() + tituloDoFilmeSelecionado
									+ EscapeANSI.NORMALIZADO.getEscapeCaracter()
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + "\":"
									+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

							this.controleDeTexto.exibirQuebraDeLinha();

							this.controleDeMenu.exibirIndicacaoParaDigitacao();

							try {

								avaliacao = entrada.nextInt();

								if (avaliacao < 1 || avaliacao > 10) {

									throw new IllegalArgumentException();

								}

								entrada.nextLine();

								this.controleDeTexto.exibirQuebraDuplaDeLinha();

								this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter() + "Sua nota "
										+ EscapeANSI.FONTE_CIANO_BRILHANTE.getEscapeCaracter() + avaliacao
										+ EscapeANSI.NORMALIZADO.getEscapeCaracter()
										+ EscapeANSI.NEGRITO.getEscapeCaracter() + " foi atribuida ao " + "\""
										+ EscapeANSI.ITALICO.getEscapeCaracter()
										+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
										+ tituloDoFilmeSelecionado + EscapeANSI.NORMALIZADO.getEscapeCaracter()
										+ EscapeANSI.NEGRITO.getEscapeCaracter() + "\"."
										+ EscapeANSI.NORMALIZADO.getEscapeCaracter());

								this.controleDeTexto.exibirQuebraDuplaDeLinha();

								this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
										+ "Agradeçemos a participação. " + EscapeANSI.NORMALIZADO.getEscapeCaracter()
										+ SimboloUnicode.ROSTO.getSimbolo() + SimboloUnicode.POSITIVO.getSimbolo());

								this.controleDeTexto.exibirQuebraDuplaDeLinha();

								insercaoDeNotaAtivada = Boolean.FALSE;

								votacaoAtivada = Boolean.FALSE;

							} catch (InputMismatchException | IllegalArgumentException e) {

								this.controleDeAviso.exibirAvisoCustomizado(
										"A nota deve ser um valor numérico inteiro entre 1 e 10.");

								entrada.nextLine();

							}
						}

					} catch (IllegalArgumentException e) {

						this.controleDeAviso.exibirAvisoDeOpcaoInvalida();

					}

				}

				break;

			}

			case "voltar": {

				opcaoResultadoDeBuscaAtivo = Boolean.FALSE;

				this.controleDeAviso.exibirAvisoDeRedirecionamento();

				break;

			}

			default:

				this.controleDeAviso.exibirAvisoDeOpcaoInvalida();

				break;

			}

		}

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

	private void getMenuDeSeries(Scanner entrada) {

		this.menuDeConteudoAtivo = Boolean.TRUE;

		while (this.menuDeConteudoAtivo) {

			this.controleDeMenu
					.definirEnunciado(EscapeANSI.NEGRITO.getEscapeCaracter() + "Opções disponíveis sobre séries:"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "top250"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver a lista das 250 séries mais bem avaliados de todos os tempo."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "top100"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver a lista das 100 séries mais populares do momento."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "buscar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para buscar por um ou mais séries específicas de acordo com seu título original."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "voltar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu de filmes e séries." + EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_CIANO_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "sair"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu principal." + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeMenu.exibirMenu();

			this.controleDeMenu.exibirIndicacaoParaDigitacao();

			this.opcoeSelecionada = entrada.nextLine();

			switch (this.opcoeSelecionada) {

			case "top250": {

				this.controleDeAviso.exibirAvisoDeEspera();

				this.imdb = ImdbAPI.TOP250_MELHORES_SERIES;

				this.extrator = ExtratorFactory.gerar(this.imdb.getExtratorDeConteudo());

				this.conteudoDaResposta = this.clienteHTTP
						.buscarDados(this.imdb.getUrlDeConexao() + this.chaveAPI.getProperty("imdb-api-key"));

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				this.apresentador = ApresentadorFactory.gerar(this.imdb.getApresentadorDeConteudo(),
						this.controleDeTexto);

				this.controleDeTexto.exibirQuebraTriplaDeLinha();

				this.controleDeTexto.exibirTexto(
						EscapeANSI.NEGRITO.getEscapeCaracter() + "Lista:" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

				this.apresentador.apresentar(listaDeConteudos);

				this.getMenuDeStickers(entrada);

				break;

			}

			case "top100": {

				this.controleDeAviso.exibirAvisoDeEspera();

				this.imdb = ImdbAPI.TOP100_SERIES_POPULARES;

				this.extrator = ExtratorFactory.gerar(this.imdb.getExtratorDeConteudo());

				this.conteudoDaResposta = this.clienteHTTP
						.buscarDados(this.imdb.getUrlDeConexao() + this.chaveAPI.getProperty("imdb-api-key"));

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				this.apresentador = ApresentadorFactory.gerar(this.imdb.getApresentadorDeConteudo(),
						this.controleDeTexto);

				this.controleDeTexto.exibirQuebraTriplaDeLinha();

				this.controleDeTexto.exibirTexto(
						EscapeANSI.NEGRITO.getEscapeCaracter() + "Lista:" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

				this.apresentador.apresentar(listaDeConteudos);

				this.getMenuDeStickers(entrada);

				break;

			}

			case "buscar": {

				this.imdb = ImdbAPI.BUSCA_SERIES;

				this.extrator = ExtratorFactory.gerar(this.imdb.getExtratorDeConteudo());

				this.apresentador = ApresentadorFactory.gerar(this.imdb.getApresentadorDeConteudo(),
						this.controleDeTexto);

				String conteudoBuscado = Default.getEmptyString();

				this.controleDeTexto.exibirQuebraDeLinha();

				this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
						+ "Digite o título original do série buscado:" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

				this.controleDeTexto.exibirQuebraDeLinha();

				this.controleDeMenu.exibirIndicacaoParaDigitacao();

				conteudoBuscado = entrada.nextLine();

				this.controleDeAviso.exibirAvisoDeEspera();

				this.conteudoDaResposta = clienteHTTP.buscarDados(this.imdb.getUrlDeConexao()
						+ this.chaveAPI.getProperty("imdb-api-key") + "/" + conteudoBuscado.replaceAll(" ", "%20"));

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				if (listaDeConteudos.size() == 0) {

					this.controleDeAviso.exibirAvisoDeBuscaSemResultados(conteudoBuscado);

				} else {

					this.controleDeAviso.exibirAvisoDeBuscaComResultados(conteudoBuscado);

					this.apresentador.apresentar(listaDeConteudos);

					this.getMenuDeResultadoDeBusca(entrada);

				}

				break;

			}

			case "voltar": {

				this.menuDeConteudoAtivo = Boolean.FALSE;

				this.controleDeAviso.exibirAvisoDeRedirecionamento();

				break;

			}

			case "sair": {

				this.menuDeConteudoAtivo = Boolean.FALSE;

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

	private void getMenuDeFilmes(Scanner entrada) {

		this.menuDeConteudoAtivo = Boolean.TRUE;

		while (this.menuDeConteudoAtivo) {

			this.controleDeMenu.definirEnunciado(EscapeANSI.NEGRITO.getEscapeCaracter()
					+ "Opções disponíveis sobre filmes:" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeMenu
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "top250"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver a lista dos 250 filmes mais bem avaliados de todos os tempo."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "top100"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver a lista dos 100 filmes mais populares do momento."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "buscar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para buscar por um ou mais filmes específicos de acordo com seu título original."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "voltar"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu de filmes e séries." + EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_CIANO_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "sair"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para voltar ao menu principal." + EscapeANSI.NORMALIZADO.getEscapeCaracter());

			this.controleDeMenu.exibirMenu();

			this.controleDeMenu.exibirIndicacaoParaDigitacao();

			this.opcoeSelecionada = entrada.nextLine();

			switch (this.opcoeSelecionada) {

			case "top250": {

				this.controleDeAviso.exibirAvisoDeEspera();

				this.imdb = ImdbAPI.TOP250_MELHORES_FILMES;

				this.extrator = ExtratorFactory.gerar(this.imdb.getExtratorDeConteudo());

				this.conteudoDaResposta = this.clienteHTTP
						.buscarDados(this.imdb.getUrlDeConexao() + this.chaveAPI.getProperty("imdb-api-key"));

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				this.apresentador = ApresentadorFactory.gerar(this.imdb.getApresentadorDeConteudo(),
						this.controleDeTexto);

				this.controleDeTexto.exibirQuebraTriplaDeLinha();

				this.controleDeTexto.exibirTexto(
						EscapeANSI.NEGRITO.getEscapeCaracter() + "Lista:" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

				this.apresentador.apresentar(listaDeConteudos);

				this.getMenuDeStickers(entrada);

				break;

			}

			case "top100": {

				this.controleDeAviso.exibirAvisoDeEspera();

				this.imdb = ImdbAPI.TOP100_FILMES_POPULARES;

				this.extrator = ExtratorFactory.gerar(this.imdb.getExtratorDeConteudo());

				this.conteudoDaResposta = this.clienteHTTP
						.buscarDados(this.imdb.getUrlDeConexao() + this.chaveAPI.getProperty("imdb-api-key"));

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				this.apresentador = ApresentadorFactory.gerar(this.imdb.getApresentadorDeConteudo(),
						this.controleDeTexto);

				this.controleDeTexto.exibirQuebraTriplaDeLinha();

				this.controleDeTexto.exibirTexto(
						EscapeANSI.NEGRITO.getEscapeCaracter() + "Lista:" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

				this.apresentador.apresentar(listaDeConteudos);

				this.getMenuDeStickers(entrada);

				break;

			}

			case "buscar": {

				this.imdb = ImdbAPI.BUSCA_FILMES;

				this.extrator = ExtratorFactory.gerar(this.imdb.getExtratorDeConteudo());

				this.apresentador = ApresentadorFactory.gerar(this.imdb.getApresentadorDeConteudo(),
						this.controleDeTexto);

				String conteudoBuscado = Default.getEmptyString();

				this.controleDeTexto.exibirQuebraDeLinha();

				this.controleDeTexto.exibirTexto(EscapeANSI.NEGRITO.getEscapeCaracter()
						+ "Digite o título original do filme buscado:" + EscapeANSI.NORMALIZADO.getEscapeCaracter());

				this.controleDeTexto.exibirQuebraDeLinha();

				this.controleDeMenu.exibirIndicacaoParaDigitacao();

				conteudoBuscado = entrada.nextLine();

				this.controleDeAviso.exibirAvisoDeEspera();

				this.conteudoDaResposta = clienteHTTP.buscarDados(this.imdb.getUrlDeConexao()
						+ this.chaveAPI.getProperty("imdb-api-key") + "/" + conteudoBuscado.replaceAll(" ", "%20"));

				this.listaDeConteudos = this.extrator.extrair(this.conteudoDaResposta);

				if (listaDeConteudos.size() == 0) {

					this.controleDeAviso.exibirAvisoDeBuscaSemResultados(conteudoBuscado);

				} else {

					this.controleDeAviso.exibirAvisoDeBuscaComResultados(conteudoBuscado);

					this.apresentador.apresentar(listaDeConteudos);

					this.getMenuDeResultadoDeBusca(entrada);

				}

				break;

			}

			case "voltar": {

				this.menuDeConteudoAtivo = Boolean.FALSE;

				this.controleDeAviso.exibirAvisoDeRedirecionamento();

				break;

			}

			case "sair": {

				this.menuDeConteudoAtivo = Boolean.FALSE;

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

	@Override
	public void iniciar(Scanner entrada) {

		this.interfaceAtiva = Boolean.TRUE;

		while (this.interfaceAtiva) {

			this.controleDeMenu
					.definirEnunciado(
							EscapeANSI.NEGRITO.getEscapeCaracter() + "Qual o tipo de conteúdo você deseja buscar?"
									+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "filme"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver as opções disponíveis referentes a filmes."
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter())
					.comOpcao(EscapeANSI.NEGRITO.getEscapeCaracter() + "- Digite ["
							+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
							+ EscapeANSI.ITALICO.getEscapeCaracter() + "série"
							+ EscapeANSI.NORMALIZADO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "] para ver as opções disponíveis referentes a séries."
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

			case "filme": {

				this.getMenuDeFilmes(entrada);

				break;

			}

			case "série": {

				this.getMenuDeSeries(entrada);

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
