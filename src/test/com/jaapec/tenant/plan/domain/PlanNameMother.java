package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.ValueObjects.PlanName;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanNameMother {

	public static PlanName create(String value) {
		return new PlanName(value);
	}

	public static PlanName random() {
		return create(MotherCreator.random().lorem().word());
	}
}
