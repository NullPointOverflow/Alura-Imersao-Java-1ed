package br.com.imersao.java.factory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.imersao.java.exception.GeracaoDeHashException;

public class HashFactory {

	private MessageDigest digestor;
	private StringBuilder texto;
	private String hashGerada;

	public HashFactory(String algorismoDeGeracao) {

		try {

			this.digestor = MessageDigest.getInstance(algorismoDeGeracao);

		} catch (NoSuchAlgorithmException e) {

			throw new GeracaoDeHashException("O algoritmo de calculo informado não consta no sistema.", e);

		}

	}

	public String gerar(String... texto) {

		this.texto = new StringBuilder();

		for (String string : texto) {

			this.texto.append(string);

		}

		this.digestor.update(this.texto.toString().getBytes());

		byte[] textoProcessado = this.digestor.digest();

		this.hashGerada = String.format("%032x", new BigInteger(1, textoProcessado));

		return this.hashGerada;

	}

}
