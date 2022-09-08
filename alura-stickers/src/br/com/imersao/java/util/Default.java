package br.com.imersao.java.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class Default {

	private static final String EMPTY_STRING = "";

	public static final String getEmptyString() {

		return EMPTY_STRING;

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
