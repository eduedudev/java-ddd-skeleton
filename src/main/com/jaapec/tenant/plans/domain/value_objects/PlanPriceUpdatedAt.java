package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class PlanPriceUpdatedAt extends DateTimeValueObject {

	public PlanPriceUpdatedAt(String value) {
		super(value);
	}

	public PlanPriceUpdatedAt() {
		super(null);
	}
}
