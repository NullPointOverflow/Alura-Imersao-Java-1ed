package br.com.imersao.java.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import br.com.imersao.java.entidade.LinguagemDeProgramacao;
import jakarta.inject.Inject;

@Repository
public class LinguagemDeProgramacaoMongoRepository implements LinguagemDeProgramacaoRepository {

	@Inject
	private final MongoOperations operador;

	public LinguagemDeProgramacaoMongoRepository(MongoOperations operador) {

		this.operador = operador;

	}

	private Query buscarLinguagemPorNomeExato(String nomeDaLinguagem) {

		return Query.query(Criteria.where("nome").regex("^\\Q" + nomeDaLinguagem + "\\E$"));

	}

	@Override
	public LinguagemDeProgramacao cadastrarLinguagem(LinguagemDeProgramacao novaLinguagem) {

		LinguagemDeProgramacao linguagemCadastrada = this.operador.insert(novaLinguagem);

		return linguagemCadastrada;
	}

	@Override
	public List<LinguagemDeProgramacao> buscarLinguagemPorNome(String nomeDaLinguagem) {

		Query query = Query.query(Criteria.where("nome").regex("^(?i)" + nomeDaLinguagem));

		List<LinguagemDeProgramacao> linguagens = this.operador.find(query, LinguagemDeProgramacao.class);

		return linguagens;

	}

	@Override
	public List<LinguagemDeProgramacao> listarLinguagensCadastradas() {

		Query query = Query.query(Criteria.where("id").exists(true)).with(Sort.by(Sort.Direction.DESC, "utilizadores"));

		List<LinguagemDeProgramacao> linguagens = this.operador.find(query, LinguagemDeProgramacao.class);

		return linguagens;

	}

	@Override
	public LinguagemDeProgramacao atualizarUtilizadores(LinguagemDeProgramacao linguagemComUtilizadores) {

		Query query = this.buscarLinguagemPorNomeExato(linguagemComUtilizadores.getNome());

		Update regraDeAtualizacao = new Update().set("utilizadores", linguagemComUtilizadores.getUtilizadores());

		FindAndModifyOptions opcoesExtras = new FindAndModifyOptions().returnNew(true).upsert(false);

		LinguagemDeProgramacao linguagem = this.operador.findAndModify(query, regraDeAtualizacao, opcoesExtras,
				LinguagemDeProgramacao.class);

		return linguagem;

	}

	@Override
	public LinguagemDeProgramacao atualizarLinguagem(LinguagemDeProgramacao linguagemAtualizada) {

		Query query = this.buscarLinguagemPorNomeExato(linguagemAtualizada.getNome());

		FindAndReplaceOptions regraDeAtualizacao = new FindAndReplaceOptions().returnNew();

		LinguagemDeProgramacao linguagem = this.operador.findAndReplace(query, linguagemAtualizada, regraDeAtualizacao,
				LinguagemDeProgramacao.class, LinguagemDeProgramacao.class);

		return linguagem;

	}

	@Override
	public void descadastrarLinguagem(String nomeDaLinguagem) {

		this.operador.remove(this.buscarLinguagemPorNomeExato(nomeDaLinguagem), LinguagemDeProgramacao.class);

	}

}
