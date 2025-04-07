package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public final class TenantName extends StringValueObject {

	public TenantName(String value) {
		super(value);
	}

	public TenantName() {
		super(null);
	}
}
