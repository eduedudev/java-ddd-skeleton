package com.jaapec.tenant.plans.domain.value_objects;

import java.math.BigDecimal;

import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.shared.domain.value_objects.BigDecimalValueObject;

public final class PlanPriceYearly extends BigDecimalValueObject {

	private static final int MIN_PRICE = 0;

	public PlanPriceYearly(BigDecimal value) {
		super(ensureValidPrice(value));
	}

	public PlanPriceYearly() {
		super(null);
	}

	private static BigDecimal ensureValidPrice(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) < MIN_PRICE) throw new NonNegativeNumberException(
			"PriceYearly",
			value.toString()
		);
		return value;
	}
}
