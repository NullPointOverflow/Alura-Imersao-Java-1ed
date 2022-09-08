package br.com.imersao.java.factory;

import br.com.imersao.java.exception.CriacaoDeStickerFactoryException;
import br.com.imersao.java.factory.sticker.StickerFactory;

public abstract class StickerAbstractFactory {

	public static StickerFactory gerar(String factoryEsperada) {

		Class<?> pseudoFactory;
		StickerFactory factory;

		try {

			pseudoFactory = Class.forName("br.com.imersao.java.factory.sticker." + factoryEsperada);

			factory = (StickerFactory) pseudoFactory.getDeclaredConstructor().newInstance();

		} catch (ClassNotFoundException e) {

			throw new CriacaoDeStickerFactoryException(
					"O gerador de figurinhas solicitado não foi localizado no sistema.", e);

		} catch (ReflectiveOperationException | RuntimeException e) {

			throw new CriacaoDeStickerFactoryException("Não foi possivel criar o gerador de figurinhas solicitado.", e);

		}

		return factory;

	}

}
