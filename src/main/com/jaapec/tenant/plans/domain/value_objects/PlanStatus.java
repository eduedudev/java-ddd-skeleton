package com.jaapec.tenant.plans.domain.value_objects;

import java.util.Objects;

public class PlanStatus {

	private final String value;

	public PlanStatus(String value) {
		this.value = value;
	}

	public PlanStatus() {
		this.value = null;
	}

	public String value() {
		return PlanStatus.status.valueOf(value).toString();
	}

	public enum status {
		ACTIVE,
		INACTIVE,
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		final PlanStatus that = (PlanStatus) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
