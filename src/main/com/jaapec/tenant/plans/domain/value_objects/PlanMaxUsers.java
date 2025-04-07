package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class PlanMaxUsers extends IntValueObject {

	private static final int MIN_USERS = 1;

	public PlanMaxUsers(Integer value) {
		super(ensureValidMaxUsers(value));
	}

	public PlanMaxUsers() {
		super(null);
	}

	private static Integer ensureValidMaxUsers(Integer value) {
		if (value.compareTo(0) < MIN_USERS) throw new MinValueException("MaxUsers", value.toString());
		return value;
	}
}
