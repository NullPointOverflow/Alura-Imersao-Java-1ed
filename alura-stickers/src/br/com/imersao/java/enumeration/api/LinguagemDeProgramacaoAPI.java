package br.com.imersao.java.enumeration.api;

public enum LinguagemDeProgramacaoAPI implements APIEnum {

	RANK("Rank das linguagens de programação mais utilizadas.",
			"https://npo-linguagens-api.herokuapp.com/linguagens-de-programacao/mais-utilizadas"),
	ATUALIZACAO_USUARIOS("Atualização do número de utilizadores de uma linguagem de programação.",
			"https://npo-linguagens-api.herokuapp.com/linguagens-de-programacao/");

	private String descricao;
	private String urlDeConexao;
	private String extratorDeConteudo;
	private String apresentadorDeConteudo;
	private String geradorDeStickers;

	private LinguagemDeProgramacaoAPI(final String descricao, final String urlDeConexao) {

		this.descricao = descricao;

		this.urlDeConexao = urlDeConexao;

		this.extratorDeConteudo = "ExtratorLinguagemDeProgramacao";

		this.apresentadorDeConteudo = "ApresentadorLinguagemDeProgramacao";

		this.geradorDeStickers = "LinguagemDeProgramacaoStickerFactory";

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
