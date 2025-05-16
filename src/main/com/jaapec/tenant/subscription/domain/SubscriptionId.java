package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.value_objects.Identifier;

public final class SubscriptionId extends Identifier {

	public SubscriptionId(String value) {
		super(value);
	}

	public SubscriptionId() {
		super(null);
	}
}
