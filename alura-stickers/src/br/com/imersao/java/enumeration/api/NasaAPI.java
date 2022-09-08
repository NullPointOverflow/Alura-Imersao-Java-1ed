package br.com.imersao.java.enumeration.api;

public enum NasaAPI implements APIEnum {

	APOD("Astronomy Picture of the Day", "https://api.nasa.gov/planetary/apod?api_key=");

	private String descricao;
	private String urlDeConexao;
	private String extratorDeConteudo;
	private String apresentadorDeConteudo;
	private String geradorDeStickers;

	private NasaAPI(final String descricao, final String urlDeConexao) {

		this.descricao = descricao;

		this.urlDeConexao = urlDeConexao;

		this.extratorDeConteudo = "ExtratorNASA";

		this.apresentadorDeConteudo = "ApresentadorNASA";

		this.geradorDeStickers = "NasaStickerFactory";

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
