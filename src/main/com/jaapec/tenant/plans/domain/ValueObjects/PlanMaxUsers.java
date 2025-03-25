package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxUsers extends IntValueObject {

	private static final int minUsers = 1;

	public PlanMaxUsers(Integer value) {
		super(ensureValidMaxUsers(value));
	}

	public PlanMaxUsers() {
		super(null);
	}

	private static Integer ensureValidMaxUsers(Integer value) {
		if (value.compareTo(0) < minUsers) throw new MinValueException("MaxUsers", value.toString());
		return value;
	}
}
