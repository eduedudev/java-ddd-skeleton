package com.jaapec.tenant.plan.domain;

import java.util.UUID;

import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.ValueObjects.*;

public final class PlanMother {

	public static Plan create(
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
		return Plan.create(
			id,
			name,
			description,
			priceMonthly,
			priceYearly,
			maxUsers,
			maxRoles,
			maxAccounts,
			maxInvoices,
			status,
			visibility,
			trialDays
		);
	}

	public static Plan fromRequest(CreatePlanCommand command) {
		return create(
			PlanIdMother.create(command.id()),
			PlanNameMother.create(command.name()),
			PlanDescriptionMother.create(command.description()),
			PlanPriceMonthlyMother.create(String.valueOf(command.priceMonthly())),
			PlanPriceYearlyMother.create(String.valueOf(command.priceYearly())),
			PlanMaxUsersMother.create(String.valueOf(command.maxUsers())),
			PlanMaxRolesMother.create(String.valueOf(command.maxRoles())),
			PlanMaxAccountsMother.create(String.valueOf(command.maxAccounts())),
			PlanMaxInvoicesMother.create(String.valueOf(command.maxInvoices())),
			PlanStatusMother.create(command.status()),
			PlanVisibilityMother.create(command.visibility()),
			PlanTrialDaysMother.create(String.valueOf(command.trialDays()))
		);
	}

	public static Plan random() {
		String name = UUID.randomUUID().toString().substring(0, 8);
		name = name.replaceAll("-", "");
		name = name.concat(PlanNameMother.random().value());
		return create(
			PlanIdMother.random(),
			PlanNameMother.create(name),
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

	public static Plan createWithStatus(String status) {
		String name = UUID.randomUUID().toString().substring(0, 8);
		name = name.replaceAll("-", "");
		name = name.concat(PlanNameMother.random().value());
		return create(
			PlanIdMother.random(),
			PlanNameMother.create(name),
			PlanDescriptionMother.random(),
			PlanPriceMonthlyMother.random(),
			PlanPriceYearlyMother.random(),
			PlanMaxUsersMother.random(),
			PlanMaxRolesMother.random(),
			PlanMaxAccountsMother.random(),
			PlanMaxInvoicesMother.random(),
			PlanStatusMother.create(status),
			PlanVisibilityMother.random(),
			PlanTrialDaysMother.random()
		);
	}

	public static Plan createWithVisibilityAndStatus(String visibility, String active) {
		String name = UUID.randomUUID().toString().substring(0, 8);
		name = name.replaceAll("-", "");
		name = name.concat(PlanNameMother.random().value());
		return create(
			PlanIdMother.random(),
			PlanNameMother.create(name),
			PlanDescriptionMother.random(),
			PlanPriceMonthlyMother.random(),
			PlanPriceYearlyMother.random(),
			PlanMaxUsersMother.random(),
			PlanMaxRolesMother.random(),
			PlanMaxAccountsMother.random(),
			PlanMaxInvoicesMother.random(),
			PlanStatusMother.create(active),
			PlanVisibilityMother.create(visibility),
			PlanTrialDaysMother.random()
		);
	}
}
