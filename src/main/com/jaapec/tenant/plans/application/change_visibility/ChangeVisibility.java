package com.jaapec.tenant.plans.application.change_visibility;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.PlanId;
import com.jaapec.tenant.plans.domain.value_objects.PlanVisibility;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Service
public final class ChangeVisibility {

	private final PlanRepository repository;
	private final EventBus eventBus;

	public ChangeVisibility(PlanRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void changeVisibility(PlanId id, PlanVisibility visibility) {
		Plan plan = repository.find(id).orElseThrow(() -> new ResourceNotExist("plan", id.value()));

		Plan newPlan = plan.changeVisibility(visibility);
		repository.update(newPlan);
		eventBus.publish(newPlan.pullDomainEvents());
	}
}
