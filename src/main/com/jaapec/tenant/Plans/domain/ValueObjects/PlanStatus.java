package com.jaapec.tenant.Plans.domain.ValueObjects;

import java.util.Objects;

public record PlanStatus(String value) {
	private enum status {
		ACTIVE,
		INACTIVE,
	}

	@Override
	public String value() {
		return PlanStatus.status.valueOf(value).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		PlanStatus that = (PlanStatus) o;
		return Objects.equals(value, that.value);
	}
}
