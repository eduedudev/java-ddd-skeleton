package com.jaapec.tenant.plans.domain.ValueObjects;

import java.math.BigDecimal;

import com.jaapec.tenant.shared.domain.ValueObjects.BigDecimalValueObject;

public final class PlanPriceMonthly extends BigDecimalValueObject {

	public PlanPriceMonthly(BigDecimal value) {
		super(value);
	}

	public PlanPriceMonthly() {
		super(null);
	}
}
