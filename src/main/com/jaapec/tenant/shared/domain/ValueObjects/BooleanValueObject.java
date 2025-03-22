package com.jaapec.tenant.shared.domain.ValueObjects;

import java.util.Objects;

public abstract class BooleanValueObject {
	private final Boolean value;

	public BooleanValueObject(Boolean value) {
		this.value = value;
	}

	public Boolean value() {
		return value;
	}

	@Override
	public String toString() {
		return "BoleanValueObject{" +
			"value=" + value +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		BooleanValueObject that = (BooleanValueObject) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
