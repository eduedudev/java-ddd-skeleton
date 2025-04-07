package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class PlanMaxAccounts extends IntValueObject {

	private static final int MIN_ACCOUNTS = 1;

	public PlanMaxAccounts(Integer value) {
		super(ensureValidMaxAccounts(value));
	}

	public PlanMaxAccounts() {
		super(null);
	}

	private static Integer ensureValidMaxAccounts(Integer value) {
		if (value.compareTo(0) < MIN_ACCOUNTS) throw new MinValueException("MaxAccounts", value.toString());
		return value;
	}
}
