package com.jaapec.tenant.Plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.DateTimeValueObject;

public final class PlanUpdatedAt extends DateTimeValueObject {

	public PlanUpdatedAt(String value) {
		super(value);
	}

	public PlanUpdatedAt() {
		super(null);
	}
}
