package com.jaapec.tenant.tenant.application.add_subscription;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Service
public final class SubscriptionAdder {

	private final PlanRepository planRepository;
	private final TenantRepository tenantRepository;
	private final EventBus eventBus;

	public SubscriptionAdder(PlanRepository planRepository, TenantRepository tenantRepository, EventBus eventBus) {
		this.planRepository = planRepository;
		this.tenantRepository = tenantRepository;
		this.eventBus = eventBus;
	}

	public void addSubscription(
		SubscriptionId subscriptionId,
		TenantId tenantId,
		PlanId planId,
		BillingInterval interval,
		SubscriptionPricing pricing,
		Currency currency,
		SubscriptionCoupon coupon,
		SubscriptionSource source,
		SubscriptionAutoRenew autoRenew
	) {
		Plan plan = planRepository.find(planId).orElseThrow(() -> new ResourceNotExist("plan", planId.value()));
		Tenant tenant = tenantRepository.find(tenantId).orElseThrow(() -> new ResourceNotExist("tenant", tenantId.value()));
		Tenant tenantWithSubscription = tenant.subscribeToPlan(
			subscriptionId,
			plan,
			interval,
			pricing,
			currency,
			coupon,
			source,
			autoRenew
		);
		tenantRepository.update(tenantWithSubscription);
		eventBus.publish(tenantWithSubscription.pullDomainEvents());
	}
}
