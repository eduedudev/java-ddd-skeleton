package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.ValueObjects.PlanId;
import com.jaapec.tenant.shared.domain.UuidMother;

public final class PlanIdMother {

	public static PlanId create(String value) {
		return new PlanId(value);
	}

	public static PlanId random() {
		return create(UuidMother.generate());
	}
}
