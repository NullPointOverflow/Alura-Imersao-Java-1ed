package br.com.imersao.java.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.imersao.java.entidade.LinguagemDeProgramacao;
import br.com.imersao.java.repository.LinguagemDeProgramacaoRepository;
import jakarta.inject.Inject;

@RestController
@RequestMapping(value = "/linguagens-de-programacao")
public class LinguagemDeProgramacaoController {

	@Inject
	private final LinguagemDeProgramacaoRepository repositorio;

	public LinguagemDeProgramacaoController(LinguagemDeProgramacaoRepository repositorio) {

		this.repositorio = repositorio;

	}

	@PostMapping(value = "/adicionar-linguagem")
	public ResponseEntity<LinguagemDeProgramacao> cadastrarLinguagem(
			@RequestBody LinguagemDeProgramacao novaLinguagem) {

		LinguagemDeProgramacao linguagemCadastrada = this.repositorio.cadastrarLinguagem(novaLinguagem);

		ResponseEntity<LinguagemDeProgramacao> resposta = new ResponseEntity<LinguagemDeProgramacao>(
				linguagemCadastrada, HttpStatus.CREATED);

		return resposta;

	}

	@GetMapping(value = "/buscar={nomeDaLinguagem}")
	public ResponseEntity<List<LinguagemDeProgramacao>> buscarLinguagensPorNome(@PathVariable String nomeDaLinguagem) {

		List<LinguagemDeProgramacao> resultado = this.repositorio.buscarLinguagemPorNome(nomeDaLinguagem);

		ResponseEntity<List<LinguagemDeProgramacao>> resposta = new ResponseEntity<List<LinguagemDeProgramacao>>(
				resultado, HttpStatus.OK);

		return resposta;

	}

	@GetMapping(value = "/mais-utilizadas")
	public ResponseEntity<List<LinguagemDeProgramacao>> listarLinguagens() {

		List<LinguagemDeProgramacao> linguagens = this.repositorio.listarLinguagensCadastradas();

		ResponseEntity<List<LinguagemDeProgramacao>> resposta = new ResponseEntity<List<LinguagemDeProgramacao>>(
				linguagens, HttpStatus.OK);

		return resposta;

	}

	@PatchMapping(value = "/{nomeDaLinguagem}/atualizar-utilizacao")
	public ResponseEntity<LinguagemDeProgramacao> atualizarUtilizadores(
			@RequestBody LinguagemDeProgramacao linguagemComUtilizadores) {

		LinguagemDeProgramacao linguagemAtualizada = this.repositorio.atualizarUtilizadores(linguagemComUtilizadores);

		ResponseEntity<LinguagemDeProgramacao> resposta = new ResponseEntity<LinguagemDeProgramacao>(
				linguagemAtualizada, HttpStatus.OK);

		return resposta;

	}

	@DeleteMapping(value = "/{nomeDaLinguagem}/remover-linguagem")
	public ResponseEntity<?> descadastrarLinguagem(@PathVariable("nomeDaLinguagem") String nomeDaLinguagem) {

		this.repositorio.descadastrarLinguagem(nomeDaLinguagem);

		return ResponseEntity.noContent().build();

	}

}
