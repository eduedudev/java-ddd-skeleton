package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxUsers extends IntValueObject {

	public PlanMaxUsers(Integer value) {
		super(value);
	}

	public PlanMaxUsers() {
		super(null);
	}
}
