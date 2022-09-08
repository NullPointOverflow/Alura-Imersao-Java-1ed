package br.com.imersao.java.extrator;
import java.util.List;

import br.com.imersao.java.record.Conteudo;

public interface ExtratorDeConteudo {

	public List<Conteudo> extrair(String conteudoEstuturado);

}
