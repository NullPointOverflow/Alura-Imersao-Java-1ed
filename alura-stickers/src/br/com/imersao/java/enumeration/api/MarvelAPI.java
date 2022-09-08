package br.com.imersao.java.enumeration.api;

public enum MarvelAPI implements APIEnum {

	PERSONAGENS("Lista de personagens pertencentes a Marvel.",
			"https://gateway.marvel.com:443/v1/public/characters?limit=100");

	private String descricao;
	private String urlDeConexao;
	private String extratorDeConteudo;
	private String apresentadorDeConteudo;
	private String geradorDeStickers;

	private MarvelAPI(final String descricao, final String urlDeConexao) {

		this.descricao = descricao;

		this.urlDeConexao = urlDeConexao;

		this.extratorDeConteudo = "ExtratorMarvel";

		this.apresentadorDeConteudo = "ApresentadorMarvel";

		this.geradorDeStickers = "MarvelStickerFactory";

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
