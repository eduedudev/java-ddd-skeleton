package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.ValueObjects.PlanVisibility;

public final class PlanVisibilityMother {

	private static final String[] visibilities = { "PUBLIC", "PRIVATE" };

	public static PlanVisibility create(String value) {
		return new PlanVisibility(value);
	}

	public static PlanVisibility random() {
		return new PlanVisibility(visibilities[Math.abs(new java.util.Random().nextInt()) % visibilities.length]);
	}
}
