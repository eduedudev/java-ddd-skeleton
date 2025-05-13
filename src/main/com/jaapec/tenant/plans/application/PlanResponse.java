package com.jaapec.tenant.plans.application;

import java.util.List;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.shared.domain.bus.query.Response;

public record PlanResponse(
	String id,
	String name,
	String description,
	List<PriceResponse> prices,
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
}
