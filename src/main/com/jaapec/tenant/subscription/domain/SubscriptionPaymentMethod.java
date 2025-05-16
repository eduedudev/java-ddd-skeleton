package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public final class SubscriptionPaymentMethod extends StringValueObject {

	public SubscriptionPaymentMethod(String value) {
		super(value);
	}

	public SubscriptionPaymentMethod() {
		super(null);
	}
}
