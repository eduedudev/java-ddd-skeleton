package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public final class SubscriptionPaymentReference extends StringValueObject {

	public SubscriptionPaymentReference(String value) {
		super(value);
	}

	public SubscriptionPaymentReference() {
		super(null);
	}
}
