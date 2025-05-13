package com.jaapec.tenant.plans.application.add_price;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Service
public final class PlanPriceAdder {

	private final PlanRepository repository;
	private final EventBus eventBus;

	public PlanPriceAdder(PlanRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void priceAdder(
		PlanId planId,
		PlanPriceId id,
		BillingInterval billingInterval,
		Amount amount,
		Currency currency
	) {
		Plan plan = repository.find(planId).orElseThrow(() -> new ResourceNotExist("plan", planId.value()));
		Plan updatedPlan = plan.addPrice(id, billingInterval, amount, currency);
		repository.update(updatedPlan);
		eventBus.publish(updatedPlan.pullDomainEvents());
	}
}
