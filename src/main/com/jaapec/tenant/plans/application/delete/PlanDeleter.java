package com.jaapec.tenant.plans.application.delete;

import java.util.Collections;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.events.PlanDeletedDomainEvent;
import com.jaapec.tenant.plans.domain.value_objects.PlanId;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Service
public final class PlanDeleter {

	private final PlanRepository repository;
	private final EventBus eventBus;

	public PlanDeleter(PlanRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void delete(PlanId id) {
		Plan plan = repository.find(id).orElseThrow(() -> new ResourceNotExist("plan", id.value()));

		repository.delete(plan);
		eventBus.publish(Collections.singletonList(new PlanDeletedDomainEvent(plan.id().toString())));
	}
}
