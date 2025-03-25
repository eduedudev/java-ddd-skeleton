package com.jaapec.tenant.plan.application.create;

import com.jaapec.tenant.plan.domain.*;
import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.plans.domain.ValueObjects.*;

public final class CreatePlanCommandMother {

	public static CreatePlanCommand create(
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
		PlanTrialDays trialDays
	) {
		return new CreatePlanCommand(
			id.value(),
			name.value(),
			description.value(),
			Double.parseDouble(priceMonthly.value().toString()),
			Double.parseDouble(priceYearly.value().toString()),
			maxUsers.value(),
			maxRoles.value(),
			maxAccounts.value(),
			maxInvoices.value(),
			status.value(),
			visibility.value(),
			trialDays.value()
		);
	}

	public static CreatePlanCommand random() {
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
			PlanTrialDaysMother.random()
		);
	}
}
