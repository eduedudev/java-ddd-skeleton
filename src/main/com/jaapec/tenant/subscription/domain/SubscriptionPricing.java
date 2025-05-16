package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class SubscriptionPricing extends IntValueObject {

	public SubscriptionPricing(int value) {
		super(value);
	}

	public SubscriptionPricing() {
		super(null);
	}
}
