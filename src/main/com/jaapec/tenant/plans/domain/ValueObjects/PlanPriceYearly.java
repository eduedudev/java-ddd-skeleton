package com.jaapec.tenant.plans.domain.ValueObjects;

import java.math.BigDecimal;

import com.jaapec.tenant.shared.domain.ValueObjects.BigDecimalValueObject;

public final class PlanPriceYearly extends BigDecimalValueObject {

	public PlanPriceYearly(BigDecimal value) {
		super(value);
	}

	public PlanPriceYearly() {
		super(null);
	}
}
