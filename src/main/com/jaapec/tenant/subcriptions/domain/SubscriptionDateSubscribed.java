package com.jaapec.tenant.subcriptions.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class SubscriptionDateSubscribed extends DateTimeValueObject {

	public SubscriptionDateSubscribed(String value) {
		super(value);
	}

	public SubscriptionDateSubscribed() {
		super(null);
	}
}
