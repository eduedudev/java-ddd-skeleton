package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public final class SubscriptionCoupon extends StringValueObject {

	public SubscriptionCoupon(String value) {
		super(value);
	}

	public SubscriptionCoupon() {
		super(null);
	}
}
