package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.value_objects.Identifier;

public final class TenantId extends Identifier {

	public TenantId(String value) {
		super(value);
	}

	public TenantId() {
		super(null);
	}
}
