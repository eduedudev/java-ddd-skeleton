package com.jaapec.tenant.plans.domain.ValueObjects;

import java.math.BigDecimal;

import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.shared.domain.ValueObjects.BigDecimalValueObject;

public final class PlanPriceMonthly extends BigDecimalValueObject {

	private static final int minPrice = 0;

	public PlanPriceMonthly(BigDecimal value) {
		super(ensureValidPrice(value));
	}

	public PlanPriceMonthly() {
		super(null);
	}

	private static BigDecimal ensureValidPrice(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) < minPrice) throw new NonNegativeNumberException(
			"PriceMonthly",
			value.toString()
		);
		return value;
	}
}
