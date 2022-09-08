package br.com.imersao.java.util;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class Default {

	private static final String EMPTY_STRING = "";
	private static final BufferedImage EMPTY_IMAGE = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);

	public static final String getEmptyString() {

		return EMPTY_STRING;

	}

	public static final BufferedImage getEmptyImage() {

		return EMPTY_IMAGE;

	}

	public static final BigDecimal getEmptyBigDecimal() {

		return BigDecimal.ZERO;

	}

	public static final <T> List<T> getEmptyList() {

		return Collections.emptyList();

	}

	public static final <K, V> Map<K, V> getEmptyMap() {

		return Collections.emptyMap();

	}

}
