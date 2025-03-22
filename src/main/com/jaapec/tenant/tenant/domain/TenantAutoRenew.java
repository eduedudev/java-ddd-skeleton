package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.BooleanValueObject;

public final class TenantAutoRenew  extends BooleanValueObject {

	public TenantAutoRenew(Boolean value) {
		super(value);
	}

	public TenantAutoRenew() {
		super(null);
	}
}
