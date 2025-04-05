package com.jaapec.tenant.plans.application;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.shared.domain.bus.query.Response;

public record PlanResponse(
	String id,
	String name,
	String description,
	double priceMonthly,
	double priceYearly,
	int maxUsers,
	int maxRoles,
	int maxAccounts,
	int maxInvoices,
	String status,
	String visibility,
	int trialDays,
	String createdAt,
	String updatedAt
)
	implements Response {
	public static PlanResponse fromAggregate(Plan plan) {
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
}
