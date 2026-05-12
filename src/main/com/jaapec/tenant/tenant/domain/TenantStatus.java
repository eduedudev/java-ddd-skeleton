package com.jaapec.tenant.tenant.domain;

import java.util.Objects;

public final class TenantStatus {

	private final String value;

	public TenantStatus(String value) {
		Status.valueOf(value); // validates on construction
		this.value = value;
	}

	TenantStatus() {
		this.value = null;
	}

	public String value() {
		return TenantStatus.Status.valueOf(value).toString();
	}

	public enum Status {
		ACTIVE,
		INACTIVE,
		PENDING,
		SUSPENDED,
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		final TenantStatus that = (TenantStatus) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
