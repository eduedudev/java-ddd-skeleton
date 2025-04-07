package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.value_objects.PlanMaxInvoices;
import com.jaapec.tenant.shared.domain.MotherCreator;

public final class PlanMaxInvoicesMother {

	public static PlanMaxInvoices create(String value) {
		return new PlanMaxInvoices(Integer.parseInt(value));
	}

	public static PlanMaxInvoices random() {
		return create(String.valueOf(MotherCreator.random().number().numberBetween(1, 100)));
	}
}
