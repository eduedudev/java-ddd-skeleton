package com.jaapec.tenant.tenants.domain;

import java.util.Objects;

public record TenantStatus(String value) {
	private enum status {
		ACTIVE,
		INACTIVE,
		PENDING,
		SUSPENDED,
	}

	@Override
	public String value() {
		return TenantStatus.status.valueOf(value).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		TenantStatus that = (TenantStatus) o;
		return Objects.equals(value, that.value);
	}
}
