package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxRoles extends IntValueObject {

	private static final int minRoles = 1;

	public PlanMaxRoles(Integer value) {
		super(ensureValidMaxRoles(value));
	}

	public PlanMaxRoles() {
		super(null);
	}

	private static Integer ensureValidMaxRoles(Integer value) {
		if (value.compareTo(0) < minRoles) throw new MinValueException("MaxRoles", value.toString());
		return value;
	}
}
