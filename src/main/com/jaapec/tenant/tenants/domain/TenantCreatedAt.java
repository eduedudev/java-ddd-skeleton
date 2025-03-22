package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.DateTimeValueObject;

public final class TenantCreatedAt extends DateTimeValueObject {

	public TenantCreatedAt(String value) {
		super(value);
	}

	public TenantCreatedAt() {
		super(null);
	}
}
