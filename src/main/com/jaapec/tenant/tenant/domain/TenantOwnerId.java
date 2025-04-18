package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.value_objects.Identifier;

public final class TenantOwnerId extends Identifier {

	public TenantOwnerId(String value) {
		super(value);
	}

	public TenantOwnerId() {
		super(null);
	}
}
