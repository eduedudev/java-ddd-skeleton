package com.jaapec.tenant.plan.domain;

import java.math.BigDecimal;

import com.jaapec.tenant.plans.domain.value_objects.PlanPriceMonthly;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanPriceMonthlyMother {

	public static PlanPriceMonthly create(String value) {
		return new PlanPriceMonthly(new BigDecimal(value));
	}

	public static PlanPriceMonthly random() {
		return create(String.valueOf(MotherCreator.random().number().randomDouble(2, 0, 100)));
	}
}
