package com.jaapec.tenant.shared.domain.value_objects;

import java.util.Objects;

public abstract class StringValueObject {

	private final String value;

	protected StringValueObject(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return "StringValueObject{" + "value='" + value + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		StringValueObject that = (StringValueObject) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
