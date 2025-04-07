package com.jaapec.tenant.plans.domain.value_objects;

import java.math.BigDecimal;

import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.shared.domain.value_objects.BigDecimalValueObject;

public final class PlanPriceMonthly extends BigDecimalValueObject {

	private static final int MIN_PRICE = 0;

	public PlanPriceMonthly(BigDecimal value) {
		super(ensureValidPrice(value));
	}

	public PlanPriceMonthly() {
		super(null);
	}

	private static BigDecimal ensureValidPrice(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) < MIN_PRICE) throw new NonNegativeNumberException(
			"PriceMonthly",
			value.toString()
		);
		return value;
	}
}
