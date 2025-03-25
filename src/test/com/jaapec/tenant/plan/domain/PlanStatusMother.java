package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.ValueObjects.PlanStatus;

public final class PlanStatusMother {

	private static final String[] statuses = { "ACTIVE", "INACTIVE" };

	public static PlanStatus create(String value) {
		return new PlanStatus(value);
	}

	public static PlanStatus random() {
		return create(statuses[Math.abs(new java.util.Random().nextInt()) % statuses.length]);
	}
}
