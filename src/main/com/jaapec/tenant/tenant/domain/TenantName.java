package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.StringValueObject;

public final class TenantName extends StringValueObject {

	public TenantName(String value) {
		super(value);
	}

	public TenantName() {
		super(null);
	}
}
