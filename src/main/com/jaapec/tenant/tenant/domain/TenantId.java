package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.Identifier;

public final class TenantId extends Identifier {

	public TenantId(String value) {
		super(value);
	}

	public TenantId() {
		super(null);
	}
}
