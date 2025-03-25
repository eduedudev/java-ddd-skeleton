package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.ValueObjects.PlanMaxRoles;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanMaxRolesMother {

	public static PlanMaxRoles create(String value) {
		return new PlanMaxRoles(Integer.parseInt(value));
	}

	public static PlanMaxRoles random() {
		return create(String.valueOf(MotherCreator.random().number().numberBetween(1, 100)));
	}
}
