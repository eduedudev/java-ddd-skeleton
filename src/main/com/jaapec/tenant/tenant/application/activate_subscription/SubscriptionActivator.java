package com.jaapec.tenant.tenant.application.activate_subscription;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Service
public final class SubscriptionActivator {

	private final TenantRepository tenantRepository;
	private final EventBus eventBus;

	public SubscriptionActivator(TenantRepository tenantRepository, EventBus eventBus) {
		this.tenantRepository = tenantRepository;
		this.eventBus = eventBus;
	}

	public void activate(
		TenantId tenantId,
		SubscriptionId subscriptionId,
		SubscriptionPaymentMethod subscriptionPaymentMethod,
		SubscriptionPaymentReference subscriptionPaymentReference
	) {
		Tenant tenant = tenantRepository.find(tenantId).orElseThrow(() -> new ResourceNotExist("tenant", tenantId.value()));
		Tenant tenantSubscriptionActivated = tenant.activateSubscription(
			subscriptionId,
			subscriptionPaymentMethod,
			subscriptionPaymentReference
		);
		tenantRepository.update(tenantSubscriptionActivated);
		eventBus.publish(tenantSubscriptionActivated.pullDomainEvents());
	}
}
