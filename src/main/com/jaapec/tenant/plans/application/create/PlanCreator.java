package com.jaapec.tenant.plans.application.create;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.ResourceAlreadyExists;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Service
public final class PlanCreator {

	private final PlanRepository repository;
	private final EventBus eventBus;

	public PlanCreator(PlanRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void create(
		PlanId id,
		PlanName name,
		PlanDescription description,
		PlanMaxUsers maxUsers,
		PlanMaxRoles maxRoles,
		PlanMaxAccounts maxAccounts,
		PlanMaxInvoices maxInvoices,
		PlanStatus status,
		PlanVisibility visibility,
		PlanTrialDays trialDays
	) {
		if (repository.uniqueField("name", name.value())) {
			throw new ResourceAlreadyExists("plan", "name", name.value());
		}

		Plan plan = Plan.create(
			id,
			name,
			description,
			maxUsers,
			maxRoles,
			maxAccounts,
			maxInvoices,
			status,
			visibility,
			trialDays
		);
		repository.save(plan);
		eventBus.publish(plan.pullDomainEvents());
	}
}
