package br.com.imersao.java.repository;

import java.util.List;

import br.com.imersao.java.entidade.LinguagemDeProgramacao;

public interface LinguagemDeProgramacaoRepository {

	public LinguagemDeProgramacao cadastrarLinguagem(LinguagemDeProgramacao novaLinguagem);

	public List<LinguagemDeProgramacao> buscarLinguagemPorNome(String nomeDaLinguagem);

	public List<LinguagemDeProgramacao> listarLinguagensCadastradas();

	public LinguagemDeProgramacao atualizarLinguagem(LinguagemDeProgramacao linguagemAtualizada);

	public LinguagemDeProgramacao atualizarUtilizadores(LinguagemDeProgramacao linguagemComUtilizadores);

	public void descadastrarLinguagem(String nomeDaLinguagem);

}
