package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.DateTimeValueObject;

public final class PlanCreatedAt extends DateTimeValueObject {

	public PlanCreatedAt(String value) {
		super(value);
	}

	public PlanCreatedAt() {
		super(null);
	}
}
