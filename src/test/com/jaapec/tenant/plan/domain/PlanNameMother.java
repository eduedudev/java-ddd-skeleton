package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.value_objects.PlanName;
import com.jaapec.tenant.shared.domain.NameMother;

public final class PlanNameMother {

	public static PlanName create(String value) {
		return new PlanName(value);
	}

	public static PlanName random() {
		return create(NameMother.random());
	}
}
