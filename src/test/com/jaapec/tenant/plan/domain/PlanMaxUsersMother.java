package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.value_objects.PlanMaxUsers;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanMaxUsersMother {

	public static PlanMaxUsers create(String value) {
		return new PlanMaxUsers(Integer.parseInt(value));
	}

	public static PlanMaxUsers random() {
		return create(String.valueOf(MotherCreator.random().number().numberBetween(1, 100)));
	}
}
