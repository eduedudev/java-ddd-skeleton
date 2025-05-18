package com.jaapec.tenant.plan.application.update;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.events.PlanUpdatedDomainEvent;
import com.jaapec.tenant.plans.domain.value_objects.*;

public final class PlanUpdateDomainEventMother {

	public static PlanUpdatedDomainEvent create(
		PlanId id,
		PlanName name,
		PlanDescription description,
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
