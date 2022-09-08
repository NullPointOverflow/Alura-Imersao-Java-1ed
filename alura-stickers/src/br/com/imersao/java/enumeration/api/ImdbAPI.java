package br.com.imersao.java.enumeration.api;

public enum ImdbAPI implements APIEnum {

	TOP250_MELHORES_FILMES("Rank 250 melhores filmes de todos os tempos.", "https://imdb-api.com/en/API/Top250Movies/"),
	TOP250_MELHORES_SERIES("Rank 250 melhores séries de todos os tempos.", "https://imdb-api.com/en/API/Top250TVs/"),
	TOP100_FILMES_POPULARES("Rank 100 filmes mais populares.", "https://imdb-api.com/en/API/MostPopularMovies/"),
	TOP100_SERIES_POPULARES("Rank 100 séries mais populares.", "https://imdb-api.com/en/API/MostPopularTVs/"),
	BUSCA_FILMES("Busca exclusiva de filme.", "https://imdb-api.com/en/API/SearchMovie/"),
	BUSCA_SERIES("Busca exclusiva de séries.", "https://imdb-api.com/en/API/SearchSeries/");

	private String descricao;
	private String urlDeConexao;
	private String extratorDeConteudo;
	private String apresentadorDeConteudo;
	private String geradorDeStickers;

	private ImdbAPI(final String descricao, final String urlDeConexao) {

		this.descricao = descricao;

		this.urlDeConexao = urlDeConexao;

		this.extratorDeConteudo = "ExtratorIMDB";

		this.apresentadorDeConteudo = "ApresentadorIMDB";

		this.geradorDeStickers = "imdb.ImdbStickerFactory";

	}

	@Override
	public String getDescricao() {

		return this.descricao;

	}

	@Override
	public String getUrlDeConexao() {

		return this.urlDeConexao;

	}

	@Override
	public String getExtratorDeConteudo() {

		return this.extratorDeConteudo;

	}

	@Override
	public String getApresentadorDeConteudo() {

		return this.apresentadorDeConteudo;

	}

	@Override
	public String getGeradorDeStickers() {

		return this.geradorDeStickers;

	}

}
