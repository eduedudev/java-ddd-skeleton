package com.jaapec.tenant.Plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.StringValueObject;

public final class PlanName extends StringValueObject {

	public PlanName(String value) {
		super(value);
	}

	public PlanName() {
		super(null);
	}
}
