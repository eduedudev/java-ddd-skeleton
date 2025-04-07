package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.value_objects.PlanDescription;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanDescriptionMother {

	public static PlanDescription create(String value) {
		return new PlanDescription(value);
	}

	public static PlanDescription random() {
		return create(MotherCreator.random().lorem().sentence());
	}
}
