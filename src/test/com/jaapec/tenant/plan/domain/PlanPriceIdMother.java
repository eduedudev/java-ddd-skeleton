package com.jaapec.tenant.plan.domain;

import java.util.UUID;

import com.jaapec.tenant.plans.domain.value_objects.PlanPriceId;

public final class PlanPriceIdMother {

	public static PlanPriceId create(String value) {
		return new PlanPriceId(value);
	}

	public static PlanPriceId random() {
		return create(UUID.randomUUID().toString());
	}
}
