package com.jaapec.tenant.plans.domain.value_objects;

import java.util.Objects;

public final class BillingInterval {

	private final String value;

	public BillingInterval(String value) {
		this.value = value;
	}

	public BillingInterval() {
		this.value = null;
	}

	public String value() {
		return intervals.valueOf(value).toString();
	}

	public enum intervals {
		MONTHLY,
		YEARLY,
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		BillingInterval that = (BillingInterval) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
