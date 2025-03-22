package com.jaapec.tenant.Plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxRoles extends IntValueObject {

	public PlanMaxRoles(Integer value) {
		super(value);
	}

	public PlanMaxRoles() {
		super(null);
	}
}
