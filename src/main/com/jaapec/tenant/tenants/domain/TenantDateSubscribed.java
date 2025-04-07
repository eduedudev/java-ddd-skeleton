package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class TenantDateSubscribed extends DateTimeValueObject {

	public TenantDateSubscribed(String value) {
		super(value);
	}

	public TenantDateSubscribed() {
		super(null);
	}
}
