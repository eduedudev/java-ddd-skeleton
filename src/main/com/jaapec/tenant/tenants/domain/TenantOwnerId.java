package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.Identifier;

public final class TenantOwnerId extends Identifier {

	public TenantOwnerId(String value) {
		super(value);
	}

	public TenantOwnerId() {
		super(null);
	}
}
