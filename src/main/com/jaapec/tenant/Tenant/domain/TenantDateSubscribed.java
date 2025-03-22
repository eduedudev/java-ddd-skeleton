package com.jaapec.tenant.Tenant.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.DateTimeValueObject;

public final class TenantDateSubscribed extends DateTimeValueObject {

	public TenantDateSubscribed(String value) {
		super(value);
	}

	public TenantDateSubscribed() {
		super(null);
	}
}
