package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.shared.domain.value_objects.Identifier;

public final class PlanPriceId extends Identifier {

	public PlanPriceId(String value) {
		super(value);
	}

	public PlanPriceId() {
		super(null);
	}
}
