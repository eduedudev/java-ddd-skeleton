package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class PlanMaxRoles extends IntValueObject {

	private static final int MIN_ROLES = 1;

	public PlanMaxRoles(Integer value) {
		super(ensureValidMaxRoles(value));
	}

	public PlanMaxRoles() {
		super(null);
	}

	private static Integer ensureValidMaxRoles(Integer value) {
		if (value.compareTo(0) < MIN_ROLES) throw new MinValueException("MaxRoles", value.toString());
		return value;
	}
}
