package com.jaapec.tenant.subcriptions.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class SubscriptionUpdateAt extends DateTimeValueObject {

	public SubscriptionUpdateAt(String value) {
		super(value);
	}

	public SubscriptionUpdateAt() {
		super(null);
	}
}
