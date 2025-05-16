package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.value_objects.BooleanValueObject;

public final class SubscriptionAutoRenew extends BooleanValueObject {

	public SubscriptionAutoRenew(Boolean value) {
		super(value);
	}

	public SubscriptionAutoRenew() {
		super(null);
	}
}
