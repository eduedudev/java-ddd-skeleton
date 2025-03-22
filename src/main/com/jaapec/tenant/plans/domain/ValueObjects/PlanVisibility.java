package com.jaapec.tenant.plans.domain.ValueObjects;

import java.util.Objects;

public record PlanVisibility(String value) {
	private enum visibility {
		PUBLIC,
		PRIVATE,
	}

	@Override
	public String value() {
		return visibility.valueOf(value).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		PlanVisibility that = (PlanVisibility) o;
		return Objects.equals(value, that.value);
	}
}
