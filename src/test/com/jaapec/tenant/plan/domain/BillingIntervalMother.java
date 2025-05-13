package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.value_objects.BillingInterval;

public final class BillingIntervalMother {

	public static BillingInterval create(String value) {
		return new BillingInterval(value);
	}

	public static BillingInterval random() {
		BillingInterval.intervals[] values = BillingInterval.intervals.values();
		BillingInterval.intervals randomInterval = values[new java.util.Random().nextInt(values.length)];
		return create(randomInterval.name());
	}
}
