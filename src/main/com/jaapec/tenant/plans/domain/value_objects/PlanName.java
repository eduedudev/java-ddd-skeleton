package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public final class PlanName extends StringValueObject {

	public PlanName(String value) {
		super(value);
	}

	public PlanName() {
		super(null);
	}
}
