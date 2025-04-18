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
		if (this == o) return true;
		if (!(o instanceof BigDecimalValueObject that)) return false;
		return this.value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
