package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class TenantCreatedAt extends DateTimeValueObject {

	public TenantCreatedAt(String value) {
		super(value);
	}

	public TenantCreatedAt() {
		super(null);
	}
}
