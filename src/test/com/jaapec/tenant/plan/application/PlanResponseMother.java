package com.jaapec.tenant.plan.application;

import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.domain.Plan;

public final class PlanResponseMother {

	public static PlanResponse create(Plan plan) {
		return new PlanResponse(
			plan.id().value(),
			plan.name().value(),
			plan.description().value(),
			plan.priceMonthly().value().doubleValue(),
			plan.priceYearly().value().doubleValue(),
			plan.maxUsers().value(),
			plan.maxRoles().value(),
			plan.maxAccounts().value(),
			plan.maxInvoices().value(),
			plan.status().value(),
			plan.visibility().value(),
			plan.trialDays().value(),
			plan.createdAt().value(),
			plan.updatedAt().value()
		);
	}

	public static PlanResponse random() {
		return create(PlanMother.random());
	}
}
