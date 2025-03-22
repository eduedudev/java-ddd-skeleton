package com.jaapec.tenant.Plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.StringValueObject;

public final class PlanDescription extends StringValueObject {

	public PlanDescription(String value) {
		super(value);
	}

	public PlanDescription() {
		super(null);
	}
}
