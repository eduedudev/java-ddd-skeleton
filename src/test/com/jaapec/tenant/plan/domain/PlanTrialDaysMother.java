package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.ValueObjects.PlanTrialDays;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanTrialDaysMother {

	public static PlanTrialDays create(String value) {
		return new PlanTrialDays(Integer.parseInt(value));
	}

	public static PlanTrialDays random() {
		return create(String.valueOf(MotherCreator.random().number().numberBetween(1, 30)));
	}
}
