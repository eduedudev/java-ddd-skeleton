package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanMaxInvoices extends IntValueObject {

	private static final int minInvoices = 1;

	public PlanMaxInvoices(Integer value) {
		super(ensureValidMaxInvoices(value));
	}

	public PlanMaxInvoices() {
		super(null);
	}

	private static Integer ensureValidMaxInvoices(Integer value) {
		if (value.compareTo(0) < minInvoices) throw new MinValueException("MaxInvoices", value.toString());
		return value;
	}
}
