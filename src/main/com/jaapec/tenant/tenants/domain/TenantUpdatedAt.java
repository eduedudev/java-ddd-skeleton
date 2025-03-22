package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.DateTimeValueObject;

public final class TenantUpdatedAt extends DateTimeValueObject {

	public TenantUpdatedAt(String value) {
		super(value);
	}

	public TenantUpdatedAt() {
		super(null);
	}
}
