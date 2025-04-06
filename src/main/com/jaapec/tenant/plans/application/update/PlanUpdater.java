package com.jaapec.tenant.plans.application.update;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.ValueObjects.*;
import com.jaapec.tenant.shared.domain.ResourceAlreadyExists;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Service
public final class PlanUpdater {

	private final PlanRepository repository;
	private final EventBus eventBus;

	public PlanUpdater(PlanRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void update(
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
		Plan plan = repository.find(id).orElseThrow(() -> new ResourceNotExist("plan", id.value()));

		if (!plan.name().equals(name) && repository.uniqueField("name", name.value())) {
			throw new ResourceAlreadyExists("plan", "name", name.value());
		}

		Plan updatedPlan = plan.update(
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
		repository.update(updatedPlan);
		eventBus.publish(updatedPlan.pullDomainEvents());
	}
}
