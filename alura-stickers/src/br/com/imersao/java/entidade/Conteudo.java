package br.com.imersao.java.entidade;

import java.util.Objects;

public record Conteudo(String titulo, String descricao, String uriImagem, int avaliacao) {

	public Conteudo(String titulo, String descricao, String uriImagem, int avaliacao) {

		this.titulo = Objects.requireNonNull(titulo, "A nome do conteúdo não pode ser nulo.");

		this.descricao = descricao;

		this.uriImagem = Objects.requireNonNull(uriImagem, "A URL da imagem do conteúdo não pode ser nula.");

		this.avaliacao = avaliacao;

	}

	public Conteudo(String titulo, String descricao, String uriImagem) {

		this(titulo, descricao, uriImagem, 0);

	}

	public Conteudo(String titulo, int avaliacao) {

		this(titulo, "Não informado", "Não informado", avaliacao);

	}

}
