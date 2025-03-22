package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.Identifier;

public final class PlanId extends Identifier {

	public PlanId(String value) {
		super(value);
	}

	public PlanId() {
		super(null);
	}
}
