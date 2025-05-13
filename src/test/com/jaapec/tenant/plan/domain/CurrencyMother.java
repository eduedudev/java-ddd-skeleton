package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.value_objects.Currency;

public final class CurrencyMother {

	public static Currency create(String value) {
		return new Currency(value);
	}

	public static Currency random() {
		Currency.currency[] values = Currency.currency.values();
		Currency.currency randomInterval = values[new java.util.Random().nextInt(values.length)];
		return create(randomInterval.name());
	}
}
