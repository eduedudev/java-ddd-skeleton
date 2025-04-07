package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.shared.domain.value_objects.Identifier;

public final class PlanId extends Identifier {

	public PlanId(String value) {
		super(value);
	}

	public PlanId() {
		super(null);
	}
}
