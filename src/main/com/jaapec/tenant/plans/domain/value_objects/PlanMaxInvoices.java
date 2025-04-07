package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class PlanMaxInvoices extends IntValueObject {

	private static final int MIN_INVOICES = 1;

	public PlanMaxInvoices(Integer value) {
		super(ensureValidMaxInvoices(value));
	}

	public PlanMaxInvoices() {
		super(null);
	}

	private static Integer ensureValidMaxInvoices(Integer value) {
		if (value.compareTo(0) < MIN_INVOICES) throw new MinValueException("MaxInvoices", value.toString());
		return value;
	}
}
