package com.jaapec.tenant.plan.application;

import java.util.List;

import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.application.PriceResponse;
import com.jaapec.tenant.plans.domain.Plan;

public final class PlanResponseMother {

	public static PlanResponse create(Plan plan) {
		List<PriceResponse> prices = plan
			.prices()
			.stream()
			.map(price ->
				PriceResponse.fromAggregate(
					price.id().value(),
					price.billingInterval().value(),
					price.amount().value(),
					price.currency().value(),
					price.createdAt().value(),
					price.updatedAt().value()
				)
			)
			.toList();
		return new PlanResponse(
			plan.id().value(),
			plan.name().value(),
			plan.description().value(),
			prices,
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
