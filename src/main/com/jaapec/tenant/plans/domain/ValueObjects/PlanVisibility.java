package com.jaapec.tenant.plans.domain.ValueObjects;

import java.util.Objects;

public class PlanVisibility {

	private final String value;

	public PlanVisibility(String value) {
		this.value = value;
	}

	public PlanVisibility() {
		this.value = null;
	}

	private enum visibility {
		PUBLIC,
		PRIVATE,
	}

	public String value() {
		return PlanVisibility.visibility.valueOf(value).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		final PlanVisibility that = (PlanVisibility) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
