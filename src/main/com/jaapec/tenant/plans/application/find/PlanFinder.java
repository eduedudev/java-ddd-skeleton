package com.jaapec.tenant.plans.application.find;

import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.PlanId;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;

@Service
public final class PlanFinder {

	private final PlanRepository repository;

	public PlanFinder(PlanRepository repository) {
		this.repository = repository;
	}

	public PlanResponse find(PlanId id) {
		return repository
			.find(id)
			.map(PlanResponse::fromAggregate)
			.orElseThrow(() -> new ResourceNotExist("plan", id.value()));
	}
}
