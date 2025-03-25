package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxAccounts extends IntValueObject {

	private static final int minAccounts = 1;

	public PlanMaxAccounts(Integer value) {
		super(ensureValidMaxAccounts(value));
	}

	public PlanMaxAccounts() {
		super(null);
	}

	private static Integer ensureValidMaxAccounts(Integer value) {
		if (value.compareTo(0) < minAccounts) throw new MinValueException("MaxAccounts", value.toString());
		return value;
	}
}
