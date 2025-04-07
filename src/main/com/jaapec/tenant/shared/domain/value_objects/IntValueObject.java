package com.jaapec.tenant.shared.domain.value_objects;

import java.util.Objects;

public abstract class IntValueObject {

	private final Integer value;

	protected IntValueObject(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	@Override
	public String toString() {
		return "IntValueObject{" + "value=" + value + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		IntValueObject that = (IntValueObject) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
