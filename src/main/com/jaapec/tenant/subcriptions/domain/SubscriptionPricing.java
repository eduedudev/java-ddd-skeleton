package com.jaapec.tenant.subcriptions.domain;

import java.math.BigDecimal;

import com.jaapec.tenant.shared.domain.value_objects.BigDecimalValueObject;

public final class SubscriptionPricing extends BigDecimalValueObject {

	public SubscriptionPricing(BigDecimal value) {
		super(value);
	}

	public SubscriptionPricing() {
		super(null);
	}
}
