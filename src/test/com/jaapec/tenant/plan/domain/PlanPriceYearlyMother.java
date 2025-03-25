package com.jaapec.tenant.plan.domain;

import java.math.BigDecimal;

import com.jaapec.tenant.plans.domain.ValueObjects.PlanPriceYearly;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanPriceYearlyMother {

	public static PlanPriceYearly create(String value) {
		return new PlanPriceYearly(new BigDecimal(value));
	}

	public static PlanPriceYearly random() {
		return create(String.valueOf(MotherCreator.random().number().randomDouble(2, 0, 100)));
	}
}
