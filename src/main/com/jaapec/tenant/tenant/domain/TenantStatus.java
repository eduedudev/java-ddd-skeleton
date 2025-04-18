package com.jaapec.tenant.tenant.domain;

import java.util.Objects;

public final class TenantStatus {

	public enum Status {
		ACTIVE,
		INACTIVE,
		PENDING,
		SUSPENDED,
	}

	private final String value;

	public TenantStatus(String value) {
		this.value = value;
	}

	public String value() {
		return TenantStatus.Status.valueOf(value).toString();
	}

	public TenantStatus() {
		this.value = null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TenantStatus that = (TenantStatus) o;
		return value == that.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
