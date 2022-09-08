
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class App {

	private static final Properties PROPRIEDADES = new Properties();

	public static void init() {

		try (InputStream leitorDeArquivo = App.class.getClassLoader().getResourceAsStream("applicacao.properties")) {

			PROPRIEDADES.load(leitorDeArquivo);

		} catch (Exception e) {

			throw new RuntimeException("Não foi possível localizar o arquivo de configuração informado.", e);

		}

	}

	public static void main(String[] args) throws Exception {

		init();

		// Fazer uma conexão HTTP e buscar os top 250 filmes
		String urlIMDBMovieTop250 = "https://imdb-api.com/en/API/Top250Movies/"
				+ PROPRIEDADES.getProperty("imdb-api-key");
//		String urlIMDBSerieTop250 = "https://imdb-api.com/en/API/Top250TVs/" + PROPRIEDADES.getProperty("imdb-api-key");
//		String urlIMDBMovieMostPopularTop100 = "https://imdb-api.com/en/API/MostPopularMovies/"
//				+ PROPRIEDADES.getProperty("imdb-api-key");
//		String urlIMDBSerieMostPopularTop100 = "https://imdb-api.com/en/API/MostPopularTVs/"
//				+ PROPRIEDADES.getProperty("imdb-api-key");

		URI endereco = URI.create(urlIMDBMovieTop250);
		HttpClient clienteWeb = HttpClient.newHttpClient();
		HttpRequest requisicao = HttpRequest.newBuilder().uri(endereco).GET().build();
		HttpResponse<String> reposta = clienteWeb.send(requisicao, BodyHandlers.ofString(StandardCharsets.UTF_8));
		String conteudoDaResposta = reposta.body();

		// Extrair só os dados que interessam (titulo, poster, classificação)
		JsonParser analisadorDeJSON = new JsonParser();
		List<Map<String, String>> listaDeFilmes = analisadorDeJSON.parseListagem(conteudoDaResposta);

		// Exibir e manipular os dados
		for (Map<String, String> conteudo : listaDeFilmes) {

			String titulo = conteudo.get("titulo");
			String urlImagem = conteudo.get("urlImagem");
			int avaliacao = new BigDecimal(conteudo.get("avaliacao")).setScale(1, RoundingMode.HALF_EVEN).intValue();

			System.out.println(EscapeANSI.FUNDO_VERMELHO.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
					+ "Titulo:" + EscapeANSI.NORMAL.getEscapeCaracter() + " " + EscapeANSI.NEGRITO.getEscapeCaracter()
					+ titulo + EscapeANSI.NORMAL.getEscapeCaracter());

			System.out.println(EscapeANSI.FUNDO_VERDE.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
					+ "Capa:" + EscapeANSI.NORMAL.getEscapeCaracter() + " " + urlImagem);

			System.out.print(EscapeANSI.FUNDO_AZUL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
					+ "Avaliação:" + EscapeANSI.NORMAL.getEscapeCaracter() + " ");

			for (int i = 0; i < avaliacao; i++) {

				System.out.print(SimboloUnicode.ESTRELA_BRILHANTE.getSimbolo());

			}

			System.out.println(System.lineSeparator());

		}

		// Sistema de votação
		try (Scanner entrada = new Scanner(System.in)) {

			boolean programaAtivo = true;
			boolean selecaoAtiva = true;
			boolean interacaoAtiva = true;
			boolean votacaoAtiva = true;
			String conteudoBuscado = "";
			String opcoeSelecionada = "";
			List<Map<String, String>> resultadoDaBusca = Collections.emptyList();
			int avaliacao = -1;

			while (programaAtivo) {

				System.out.print(EscapeANSI.NEGRITO.getEscapeCaracter() + "Qual o tipo de conteúdo você deseja buscar?"
						+ System.lineSeparator() + System.lineSeparator() + "- Digite ["
						+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
						+ EscapeANSI.ITALICO.getEscapeCaracter() + "filme" + EscapeANSI.NORMAL.getEscapeCaracter()
						+ EscapeANSI.NEGRITO.getEscapeCaracter() + "] para realizar a buscar por filmes."
						+ System.lineSeparator() + "- Digite [" + EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter()
						+ EscapeANSI.ITALICO.getEscapeCaracter() + "série" + EscapeANSI.NORMAL.getEscapeCaracter()
						+ EscapeANSI.NEGRITO.getEscapeCaracter() + "] para realizar a buscar por séries."
						+ System.lineSeparator() + "- Digite [" + EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter()
						+ EscapeANSI.ITALICO.getEscapeCaracter() + "sair" + EscapeANSI.NORMAL.getEscapeCaracter()
						+ EscapeANSI.NEGRITO.getEscapeCaracter() + "] para encerrar o programa."
						+ System.lineSeparator() + EscapeANSI.PISCANTE.getEscapeCaracter() + "> "
						+ EscapeANSI.NORMAL.getEscapeCaracter());

				opcoeSelecionada = entrada.nextLine();

				switch (opcoeSelecionada) {

				case "filme": {

					int numeroDoFilme;
					String nomeDoFilmeSelecionado;

					System.out.print(System.lineSeparator() + System.lineSeparator()
							+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Digite o título original do filme buscado:"
							+ System.lineSeparator() + EscapeANSI.PISCANTE.getEscapeCaracter() + "> "
							+ EscapeANSI.NORMAL.getEscapeCaracter());

					conteudoBuscado = entrada.nextLine();

					endereco = URI.create("https://imdb-api.com/en/API/SearchMovie/"
							+ PROPRIEDADES.getProperty("imdb-api-key") + "/" + conteudoBuscado);
					requisicao = HttpRequest.newBuilder().uri(endereco).GET().build();
					reposta = clienteWeb.send(requisicao, BodyHandlers.ofString(StandardCharsets.UTF_8));
					conteudoDaResposta = reposta.body();

					resultadoDaBusca = new JsonParser().parseResultadoDeBusca(conteudoDaResposta);

					if (resultadoDaBusca.size() == 0) {

						System.out.println(System.lineSeparator() + System.lineSeparator()
								+ EscapeANSI.NEGRITO.getEscapeCaracter()
								+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
								+ "Nenhum resultado encontrado para o título \"" + EscapeANSI.NORMAL.getEscapeCaracter()
								+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + conteudoBuscado
								+ EscapeANSI.NORMAL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
								+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + "\"."
								+ System.lineSeparator() + EscapeANSI.NORMAL.getEscapeCaracter());

						break;

					} else {

						System.out.println(System.lineSeparator() + System.lineSeparator()
								+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Resultado(s) da busca por \""
								+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + conteudoBuscado
								+ EscapeANSI.NORMAL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter() + "\":"
								+ System.lineSeparator());

						for (int i = 0; i < resultadoDaBusca.size(); i++) {

							String titulo = resultadoDaBusca.get(i).get("titulo");
							String urlImagem = resultadoDaBusca.get(i).get("urlImagem");
							String descricao = resultadoDaBusca.get(i).get("descricao");

							System.out.println(EscapeANSI.NEGRITO.getEscapeCaracter() + "Resultado " + (i + 1)
									+ EscapeANSI.NORMAL.getEscapeCaracter());

							System.out.println(EscapeANSI.FUNDO_VERMELHO.getEscapeCaracter()
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Titulo:"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + " "
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + titulo
									+ EscapeANSI.NORMAL.getEscapeCaracter());

							System.out.println(EscapeANSI.FUNDO_AZUL_BRILHANTE.getEscapeCaracter()
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Descrição:"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + " " + descricao);

							System.out.println(EscapeANSI.FUNDO_VERDE_BRILHANTE.getEscapeCaracter()
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Capa:"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + " " + urlImagem);

							System.out.println(System.lineSeparator());

						}

						while (selecaoAtiva) {

							System.out.print(EscapeANSI.NEGRITO.getEscapeCaracter()
									+ "Digite o número correspondende ao filme para ver as opções de interação ou digite ["
									+ EscapeANSI.ITALICO.getEscapeCaracter()
									+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter() + "sair"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
									+ "] para encerrar o programa:" + System.lineSeparator()
									+ EscapeANSI.PISCANTE.getEscapeCaracter() + "> "
									+ EscapeANSI.NORMAL.getEscapeCaracter());

							try {

								opcoeSelecionada = entrada.nextLine();

								if (opcoeSelecionada.equals("sair")) {

									selecaoAtiva = false;

									programaAtivo = false;

									System.out.println(System.lineSeparator() + EscapeANSI.NEGRITO.getEscapeCaracter()
											+ "Programa encerrado. " + SimboloUnicode.ACENO.getSimbolo()
											+ EscapeANSI.NORMAL.getEscapeCaracter() + System.lineSeparator());

									break;

								} else {

									numeroDoFilme = Integer.parseInt(opcoeSelecionada) - 1;

									if (numeroDoFilme < 0 || numeroDoFilme >= resultadoDaBusca.size()) {

										throw new IllegalArgumentException();

									}

									nomeDoFilmeSelecionado = resultadoDaBusca.get(numeroDoFilme).get("titulo");

									while (interacaoAtiva) {

										System.out.print(System.lineSeparator() + System.lineSeparator()
												+ EscapeANSI.NEGRITO.getEscapeCaracter()
												+ "Opções de interação disponíveis para o filme selecionado:"
												+ System.lineSeparator() + "- Digite ["
												+ EscapeANSI.ITALICO.getEscapeCaracter()
												+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter() + "votar"
												+ EscapeANSI.NORMAL.getEscapeCaracter()
												+ EscapeANSI.NEGRITO.getEscapeCaracter()
												+ "] para atribuir uma nota ao filme;" + System.lineSeparator()
												+ "- Digite [" + EscapeANSI.ITALICO.getEscapeCaracter()
												+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter() + "sair"
												+ EscapeANSI.NORMAL.getEscapeCaracter()
												+ EscapeANSI.NEGRITO.getEscapeCaracter() + "] para encerrar o programa;"
												+ System.lineSeparator() + EscapeANSI.PISCANTE.getEscapeCaracter()
												+ "> " + EscapeANSI.NORMAL.getEscapeCaracter());

										opcoeSelecionada = entrada.nextLine();

										switch (opcoeSelecionada) {

										case "votar": {

											while (votacaoAtiva) {

												System.out.print(System.lineSeparator() + System.lineSeparator()
														+ EscapeANSI.NEGRITO.getEscapeCaracter()
														+ "Entre com uma nota (de 1 a 10) para o filme " + "\""
														+ nomeDoFilmeSelecionado + "\"" + ":"
														+ EscapeANSI.NORMAL.getEscapeCaracter() + System.lineSeparator()
														+ EscapeANSI.PISCANTE.getEscapeCaracter() + "> "
														+ EscapeANSI.NORMAL.getEscapeCaracter());

												try {

													avaliacao = entrada.nextInt();

													if (avaliacao < 1 || avaliacao > 10) {

														throw new IllegalArgumentException();

													}

													System.out.println(System.lineSeparator() + System.lineSeparator()
															+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Sua nota "
															+ avaliacao + " foi atribuida ao filme " + "\""
															+ nomeDoFilmeSelecionado + "\""
															+ EscapeANSI.NORMAL.getEscapeCaracter() + ".");

													System.out.println(System.lineSeparator()
															+ EscapeANSI.NEGRITO.getEscapeCaracter()
															+ "Agradeçemos a participação. "
															+ EscapeANSI.NORMAL.getEscapeCaracter()
															+ SimboloUnicode.ROSTO.getSimbolo()
															+ SimboloUnicode.POSITIVO.getSimbolo());

													votacaoAtiva = false;

													interacaoAtiva = false;

													selecaoAtiva = false;

													programaAtivo = false;

													break;

												} catch (IllegalArgumentException | InputMismatchException e) {

													System.out.println(System.lineSeparator()
															+ SimboloUnicode.AVISO.getSimbolo()
															+ EscapeANSI.NEGRITO.getEscapeCaracter()
															+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
															+ " A nota deve ser um valor numérico inteiro entre 1 e 10. "
															+ EscapeANSI.NORMAL.getEscapeCaracter()
															+ SimboloUnicode.AVISO.getSimbolo()
															+ System.lineSeparator());

													entrada.nextLine();

												}

											}

											break;

										}

										case "sair": {

											interacaoAtiva = false;

											selecaoAtiva = false;

											programaAtivo = false;

											System.out.println(System.lineSeparator()
													+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Programa encerrado. "
													+ SimboloUnicode.ACENO.getSimbolo()
													+ EscapeANSI.NORMAL.getEscapeCaracter() + System.lineSeparator());

											break;

										}

										default:

											System.out.println(System.lineSeparator()
													+ SimboloUnicode.PROIBIDO.getSimbolo()
													+ EscapeANSI.NEGRITO.getEscapeCaracter()
													+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
													+ " Opção inválida. " + EscapeANSI.NORMAL.getEscapeCaracter()
													+ SimboloUnicode.PROIBIDO.getSimbolo() + System.lineSeparator());

											break;

										}

									}

								}

							} catch (InputMismatchException | IllegalArgumentException e) {

								System.out.println(System.lineSeparator() + SimboloUnicode.PROIBIDO.getSimbolo()
										+ EscapeANSI.NEGRITO.getEscapeCaracter()
										+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + " Opção inválida. "
										+ EscapeANSI.NORMAL.getEscapeCaracter() + SimboloUnicode.PROIBIDO.getSimbolo()
										+ System.lineSeparator());

							}

						}

					}

					break;

				}

				case "série": {

					int numeroDaSerie;
					String nomeDaSerieSelecionada;

					System.out.print(System.lineSeparator() + System.lineSeparator()
							+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Digite o título original da série buscada:"
							+ System.lineSeparator() + EscapeANSI.PISCANTE.getEscapeCaracter() + "> "
							+ EscapeANSI.NORMAL.getEscapeCaracter());

					conteudoBuscado = entrada.nextLine();

					endereco = URI.create("https://imdb-api.com/en/API/SearchSeries/"
							+ PROPRIEDADES.getProperty("imdb-api-key") + "/" + conteudoBuscado);
					requisicao = HttpRequest.newBuilder().uri(endereco).GET().build();
					reposta = clienteWeb.send(requisicao, BodyHandlers.ofString(StandardCharsets.UTF_8));
					conteudoDaResposta = reposta.body();

					resultadoDaBusca = new JsonParser().parseResultadoDeBusca(conteudoDaResposta);

					if (resultadoDaBusca.size() == 0) {

						System.out.println(System.lineSeparator() + System.lineSeparator()
								+ EscapeANSI.NEGRITO.getEscapeCaracter()
								+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
								+ "Nenhum resultado encontrado para o título \"" + EscapeANSI.NORMAL.getEscapeCaracter()
								+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + conteudoBuscado
								+ EscapeANSI.NORMAL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
								+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + "\"."
								+ System.lineSeparator() + EscapeANSI.NORMAL.getEscapeCaracter());

						break;

					} else {

						System.out.println(System.lineSeparator() + System.lineSeparator()
								+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Resultado(s) da busca por \""
								+ EscapeANSI.FONTE_AMARELA_BRILHANTE.getEscapeCaracter()
								+ EscapeANSI.ITALICO.getEscapeCaracter() + conteudoBuscado
								+ EscapeANSI.NORMAL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter() + "\":"
								+ System.lineSeparator());

						for (int i = 0; i < resultadoDaBusca.size(); i++) {

							String titulo = resultadoDaBusca.get(i).get("titulo");
							String urlImagem = resultadoDaBusca.get(i).get("urlImagem");
							String descricao = resultadoDaBusca.get(i).get("descricao");

							System.out.println(EscapeANSI.NEGRITO.getEscapeCaracter() + "Resultado " + (i + 1)
									+ EscapeANSI.NORMAL.getEscapeCaracter());

							System.out.println(EscapeANSI.FUNDO_VERMELHO.getEscapeCaracter()
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Titulo:"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + " "
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + titulo
									+ EscapeANSI.NORMAL.getEscapeCaracter());

							System.out.println(EscapeANSI.FUNDO_AZUL_BRILHANTE.getEscapeCaracter()
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Descrição:"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + " " + descricao);

							System.out.println(EscapeANSI.FUNDO_VERDE_BRILHANTE.getEscapeCaracter()
									+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Capa:"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + " " + urlImagem);

							System.out.println(System.lineSeparator());

						}

						while (selecaoAtiva) {

							System.out.print(EscapeANSI.NEGRITO.getEscapeCaracter()
									+ "Digite o número correspondende a série para ver as opções de interação ou digite ["
									+ EscapeANSI.ITALICO.getEscapeCaracter()
									+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter() + "sair"
									+ EscapeANSI.NORMAL.getEscapeCaracter() + EscapeANSI.NEGRITO.getEscapeCaracter()
									+ "] para encerrar o programa:" + System.lineSeparator()
									+ EscapeANSI.PISCANTE.getEscapeCaracter() + "> "
									+ EscapeANSI.NORMAL.getEscapeCaracter());

							try {

								opcoeSelecionada = entrada.nextLine();

								if (opcoeSelecionada.equals("sair")) {

									selecaoAtiva = false;

									programaAtivo = false;

									System.out.println(System.lineSeparator() + EscapeANSI.NEGRITO.getEscapeCaracter()
											+ "Programa encerrado. " + SimboloUnicode.ACENO.getSimbolo()
											+ EscapeANSI.NORMAL.getEscapeCaracter() + System.lineSeparator());

									break;

								} else {

									numeroDaSerie = Integer.parseInt(opcoeSelecionada) - 1;

									if (numeroDaSerie < 0 || numeroDaSerie >= resultadoDaBusca.size()) {

										throw new IllegalArgumentException();

									}

									nomeDaSerieSelecionada = resultadoDaBusca.get(numeroDaSerie).get("titulo");

									while (interacaoAtiva) {

										System.out.print(System.lineSeparator() + System.lineSeparator()
												+ EscapeANSI.NEGRITO.getEscapeCaracter()
												+ "Opções de interação disponíveis para a série selecionada:"
												+ System.lineSeparator() + "- Digite ["
												+ EscapeANSI.ITALICO.getEscapeCaracter()
												+ EscapeANSI.FONTE_VERDE_BRILHANTE.getEscapeCaracter() + "votar"
												+ EscapeANSI.NORMAL.getEscapeCaracter()
												+ EscapeANSI.NEGRITO.getEscapeCaracter()
												+ "] para atribuir uma nota a série;" + System.lineSeparator()
												+ "- Digite [" + EscapeANSI.ITALICO.getEscapeCaracter()
												+ EscapeANSI.FONTE_AZUL_BRILHANTE.getEscapeCaracter() + "sair"
												+ EscapeANSI.NORMAL.getEscapeCaracter()
												+ EscapeANSI.NEGRITO.getEscapeCaracter() + "] para encerrar o programa;"
												+ System.lineSeparator() + EscapeANSI.PISCANTE.getEscapeCaracter()
												+ "> " + EscapeANSI.NORMAL.getEscapeCaracter());

										opcoeSelecionada = entrada.nextLine();

										switch (opcoeSelecionada) {

										case "votar": {

											while (votacaoAtiva) {

												System.out.print(System.lineSeparator() + System.lineSeparator()
														+ EscapeANSI.NEGRITO.getEscapeCaracter()
														+ "Entre com uma nota (de 1 a 10) para a série " + "\""
														+ nomeDaSerieSelecionada + "\"" + ":"
														+ EscapeANSI.NORMAL.getEscapeCaracter() + System.lineSeparator()
														+ EscapeANSI.PISCANTE.getEscapeCaracter() + "> "
														+ EscapeANSI.NORMAL.getEscapeCaracter());

												try {

													avaliacao = entrada.nextInt();

													if (avaliacao < 1 || avaliacao > 10) {

														throw new IllegalArgumentException();

													}

													System.out.println(System.lineSeparator() + System.lineSeparator()
															+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Sua nota "
															+ avaliacao + " foi atribuida a série " + "\""
															+ nomeDaSerieSelecionada + "\""
															+ EscapeANSI.NORMAL.getEscapeCaracter() + ".");

													System.out.println(System.lineSeparator()
															+ EscapeANSI.NEGRITO.getEscapeCaracter()
															+ "Agradeçemos a participação. "
															+ EscapeANSI.NORMAL.getEscapeCaracter()
															+ SimboloUnicode.ROSTO.getSimbolo()
															+ SimboloUnicode.POSITIVO.getSimbolo());

													votacaoAtiva = false;

													interacaoAtiva = false;

													selecaoAtiva = false;

													programaAtivo = false;

													break;

												} catch (IllegalArgumentException | InputMismatchException e) {

													System.out.println(System.lineSeparator()
															+ SimboloUnicode.AVISO.getSimbolo()
															+ EscapeANSI.NEGRITO.getEscapeCaracter()
															+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
															+ " A nota deve ser um valor numérico inteiro entre 1 e 10. "
															+ EscapeANSI.NORMAL.getEscapeCaracter()
															+ SimboloUnicode.AVISO.getSimbolo()
															+ System.lineSeparator());

													entrada.nextLine();

												}

											}

											break;

										}

										case "sair": {

											interacaoAtiva = false;

											selecaoAtiva = false;

											programaAtivo = false;

											System.out.println(System.lineSeparator()
													+ EscapeANSI.NEGRITO.getEscapeCaracter() + "Programa encerrado. "
													+ SimboloUnicode.ACENO.getSimbolo()
													+ EscapeANSI.NORMAL.getEscapeCaracter() + System.lineSeparator());

											break;

										}

										default:

											System.out.println(System.lineSeparator()
													+ SimboloUnicode.PROIBIDO.getSimbolo()
													+ EscapeANSI.NEGRITO.getEscapeCaracter()
													+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter()
													+ " Opção inválida. " + EscapeANSI.NORMAL.getEscapeCaracter()
													+ SimboloUnicode.PROIBIDO.getSimbolo() + System.lineSeparator());

											break;

										}

									}

								}

							} catch (InputMismatchException | IllegalArgumentException e) {

								System.out.println(System.lineSeparator() + SimboloUnicode.PROIBIDO.getSimbolo()
										+ EscapeANSI.NEGRITO.getEscapeCaracter()
										+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + " Opção inválida. "
										+ EscapeANSI.NORMAL.getEscapeCaracter() + SimboloUnicode.PROIBIDO.getSimbolo()
										+ System.lineSeparator());

							}

						}

					}

					break;

				}

				case "sair": {

					programaAtivo = false;

					System.out.println(System.lineSeparator() + EscapeANSI.NEGRITO.getEscapeCaracter()
							+ "Programa encerrado. " + SimboloUnicode.ACENO.getSimbolo()
							+ EscapeANSI.NORMAL.getEscapeCaracter() + System.lineSeparator());

					break;

				}

				default:

					System.out.println(System.lineSeparator() + SimboloUnicode.PROIBIDO.getSimbolo()
							+ EscapeANSI.NEGRITO.getEscapeCaracter()
							+ EscapeANSI.FONTE_VERMELHA_BRILHANTE.getEscapeCaracter() + " Opção inválida. "
							+ EscapeANSI.NORMAL.getEscapeCaracter() + SimboloUnicode.PROIBIDO.getSimbolo()
							+ System.lineSeparator());

					break;

				}

			}

		}

	}

}
