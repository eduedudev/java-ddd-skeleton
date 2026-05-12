package com.jaapec.tenant.plans.domain.value_objects;

import java.time.LocalDateTime;
import java.util.Objects;

public final class BillingInterval {

	private final String value;

	public BillingInterval(String value) {
		intervals.valueOf(value.toUpperCase()); // validates on construction
		this.value = value.toUpperCase();
	}

	BillingInterval() {
		this.value = null;
	}

	public String value() {
		return intervals.valueOf(value).toString();
	}

	public LocalDateTime calculateExpiration(LocalDateTime from) {
		return switch (intervals.valueOf(value)) {
			case MONTHLY -> from.plusMonths(1);
			case YEARLY -> from.plusYears(1);
		};
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
