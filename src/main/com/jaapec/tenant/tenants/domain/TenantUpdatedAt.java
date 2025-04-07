package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class TenantUpdatedAt extends DateTimeValueObject {

	public TenantUpdatedAt(String value) {
		super(value);
	}

	public TenantUpdatedAt() {
		super(null);
	}
}
