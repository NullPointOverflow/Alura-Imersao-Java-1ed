package br.com.imersao.java.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface Arredondamento {

	default int arredondarValor(double valor) {

		return new BigDecimal(valor).setScale(0, RoundingMode.HALF_EVEN).intValue();

	}

}
