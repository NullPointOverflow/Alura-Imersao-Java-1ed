package br.com.imersao.java.entidade;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "programacao")
public class LinguagemDeProgramacao {

	@Id
	private String id;

	@Field(value = "nome")
	@Indexed(unique = true)
	private String nome;

	@Field(value = "descricao")
	private String descricao;

	@Field(value = "logotipo")
	private String logotipo;

	@Field(value = "utilizadores")
	private int utilizadores;

	public LinguagemDeProgramacao() {
	}

	public LinguagemDeProgramacao(String nome, String descricao, String logotipo, int utilizadores) {

		this.nome = Objects.requireNonNull(nome, "O nome da linguagem não foi informado.");

		this.descricao = descricao;

		this.logotipo = Objects.requireNonNull(logotipo, "O logotipo do linguagem não foi informado.");

		this.utilizadores = utilizadores;

	}

	public String getId() {

		return this.id;

	}

	public String getNome() {

		return this.nome;

	}

	public String getDescricao() {

		return this.descricao;

	}

	public String getLogotipo() {

		return this.logotipo;

	}

	public int getUtilizadores() {

		return this.utilizadores;

	}

}
