package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class SubscriptionInitDate extends DateTimeValueObject {

	public SubscriptionInitDate(String value) {
		super(value);
	}

	public SubscriptionInitDate() {
		super(null);
	}
}
