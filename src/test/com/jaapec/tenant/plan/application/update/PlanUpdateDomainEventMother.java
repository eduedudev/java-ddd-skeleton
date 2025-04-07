package com.jaapec.tenant.plan.application.update;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.events.PlanUpdatedDomainEvent;
import com.jaapec.tenant.plans.domain.value_objects.*;

public class PlanUpdateDomainEventMother {

	public static PlanUpdatedDomainEvent create(
		PlanId id,
		PlanName name,
		PlanDescription description,
		PlanPriceMonthly priceMonthly,
		PlanPriceYearly priceYearly,
		PlanMaxUsers maxUsers,
		PlanMaxRoles maxRoles,
		PlanMaxAccounts maxAccounts,
		PlanMaxInvoices maxInvoices,
		PlanStatus status,
		PlanVisibility visibility,
		PlanTrialDays trialDays,
		PlanCreatedAt createdAt,
		PlanUpdatedAt updatedAt
	) {
		return new PlanUpdatedDomainEvent(
			id.value(),
			name.value(),
			description.value(),
			priceMonthly.value().toString(),
			priceYearly.value().toString(),
			maxUsers.value().toString(),
			maxRoles.value().toString(),
			maxAccounts.value().toString(),
			maxInvoices.value().toString(),
			status.value(),
			visibility.value(),
			trialDays.value().toString(),
			createdAt.value(),
			updatedAt.value()
		);
	}

	public static PlanUpdatedDomainEvent fromPlan(PlanId id, Plan plan) {
		return create(
			id,
			plan.name(),
			plan.description(),
			plan.priceMonthly(),
			plan.priceYearly(),
			plan.maxUsers(),
			plan.maxRoles(),
			plan.maxAccounts(),
			plan.maxInvoices(),
			plan.status(),
			plan.visibility(),
			plan.trialDays(),
			plan.createdAt(),
			plan.updatedAt()
		);
	}
}
