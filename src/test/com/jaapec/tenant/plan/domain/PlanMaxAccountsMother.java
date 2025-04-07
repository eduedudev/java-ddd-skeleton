package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.value_objects.PlanMaxAccounts;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanMaxAccountsMother {

	public static PlanMaxAccounts create(String value) {
		return new PlanMaxAccounts(Integer.parseInt(value));
	}

	public static PlanMaxAccounts random() {
		return create(String.valueOf(MotherCreator.random().number().numberBetween(1, 100)));
	}
}
