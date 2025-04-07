package com.jaapec.tenant.shared.domain.value_objects;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class BigDecimalValueObject {

	private final BigDecimal value;

	protected BigDecimalValueObject(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal value() {
		return value;
	}

	@Override
	public String toString() {
		return "BigDecimalValueObject{" + "value=" + value + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		BigDecimalValueObject that = (BigDecimalValueObject) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
