package com.jaapec.tenant.plans.domain.value_objects;

import java.util.Objects;

public final class Currency {

	private final String value;

	public Currency(String value) {
		this.value = value;
	}

	public Currency() {
		this.value = null;
	}

	public String value() {
		return Currency.currency.valueOf(value).toString();
	}

	public enum currency {
		USD,
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Currency currency = (Currency) o;
		return Objects.equals(value, currency.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
