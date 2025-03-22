package com.jaapec.tenant.Plans.domain.ValueObjects;

import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxInvoices extends IntValueObject {

	public PlanMaxInvoices(Integer value) {
		super(value);
	}

	public PlanMaxInvoices() {
		super(null);
	}
}
