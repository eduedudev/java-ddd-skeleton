package com.jaapec.tenant.plan.domain;

import java.util.Random;

import com.jaapec.tenant.plans.domain.value_objects.Amount;

public final class AmountMother {

	private static final Random RANDOM = new Random();

	public static Amount create(int value) {
		return new Amount(value);
	}

	public static Amount random() {
		return create(RANDOM.nextInt(1000) + 1);
	}
}
