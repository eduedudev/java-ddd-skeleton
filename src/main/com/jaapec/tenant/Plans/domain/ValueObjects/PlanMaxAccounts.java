package com.jaapec.tenant.Plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxAccounts extends IntValueObject {

	public PlanMaxAccounts(Integer value) {
		super(value);
	}

	public PlanMaxAccounts() {
		super(null);
	}
}
