package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class TenantExpirationDate extends DateTimeValueObject {

	public TenantExpirationDate(String value) {
		super(value);
	}

	public TenantExpirationDate() {
		super(null);
	}
}
