package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class Amount extends IntValueObject {

	private static final int MIN_PRICE = 0;

	public Amount(Integer value) {
		super(ensureValidPrice(value));
	}

	public Amount() {
		super(null);
	}

	private static Integer ensureValidPrice(Integer value) {
		if (value < MIN_PRICE) throw new NonNegativeNumberException("Amount", value.toString());
		return value;
	}
}
