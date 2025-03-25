package com.jaapec.tenant.plan.domain;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.ValueObjects.*;
import com.jaapec.tenant.plans.domain.events.PlanCreatedDomainEvent;
import com.jaapec.tenant.shared.domain.CurrentDate;

public final class PlanCreatedDomainEventMother {

	public static PlanCreatedDomainEvent create(
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
		String now = CurrentDate.now();
		return new PlanCreatedDomainEvent(
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

	public static PlanCreatedDomainEvent fromPlan(Plan plan) {
		return create(
			plan.id(),
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

	public static PlanCreatedDomainEvent random() {
		final String now = CurrentDate.now();
		return create(
			PlanIdMother.random(),
			PlanNameMother.random(),
			PlanDescriptionMother.random(),
			PlanPriceMonthlyMother.random(),
			PlanPriceYearlyMother.random(),
			PlanMaxUsersMother.random(),
			PlanMaxRolesMother.random(),
			PlanMaxAccountsMother.random(),
			PlanMaxInvoicesMother.random(),
			PlanStatusMother.random(),
			PlanVisibilityMother.random(),
			PlanTrialDaysMother.random(),
			new PlanCreatedAt(now),
			new PlanUpdatedAt(now)
		);
	}
}
