package com.jaapec.tenant.tenants.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class TenantGracePeriodDays extends IntValueObject {
	public TenantGracePeriodDays(Integer value) {
		super(value);
	}

	public TenantGracePeriodDays() {
		super(null);
	}
}
