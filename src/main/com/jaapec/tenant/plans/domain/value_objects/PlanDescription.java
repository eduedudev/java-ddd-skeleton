package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public final class PlanDescription extends StringValueObject {

	public PlanDescription(String value) {
		super(value);
	}

	public PlanDescription() {
		super(null);
	}
}
