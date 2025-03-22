package com.jaapec.tenant.Tenant.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.DateTimeValueObject;

public final class TenantExpirationDate extends DateTimeValueObject {
	public TenantExpirationDate(String value) {
		super(value);
	}

	public TenantExpirationDate() {
		super(null);
	}
}
