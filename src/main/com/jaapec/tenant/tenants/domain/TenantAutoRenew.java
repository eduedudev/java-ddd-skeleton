package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.value_objects.BooleanValueObject;

public final class TenantAutoRenew extends BooleanValueObject {

	public TenantAutoRenew(Boolean value) {
		super(value);
	}

	public TenantAutoRenew() {
		super(null);
	}
}
