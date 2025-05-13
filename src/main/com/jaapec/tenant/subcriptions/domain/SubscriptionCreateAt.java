package com.jaapec.tenant.subcriptions.domain;

import com.jaapec.tenant.shared.domain.value_objects.DateTimeValueObject;

public final class SubscriptionCreateAt extends DateTimeValueObject {

	public SubscriptionCreateAt(String value) {
		super(value);
	}

	public SubscriptionCreateAt() {
		super(null);
	}
}
