package com.jaapec.tenant.plans.domain.ValueObjects;

import java.math.BigDecimal;

import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.shared.domain.ValueObjects.BigDecimalValueObject;

public final class PlanPriceYearly extends BigDecimalValueObject {

	private static final int minPrice = 0;

	public PlanPriceYearly(BigDecimal value) {
		super(ensureValidPrice(value));
	}

	public PlanPriceYearly() {
		super(null);
	}

	private static BigDecimal ensureValidPrice(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) < minPrice) throw new NonNegativeNumberException(
			"PriceYearly",
			value.toString()
		);
		return value;
	}
}
