package com.jaapec.tenant.tenant.application.cancel_auto_renew;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.subscription.domain.SubscriptionId;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Service
public final class SubscriptionAutoRenewCanceler {

	private final TenantRepository repository;
	private final EventBus eventBus;

	public SubscriptionAutoRenewCanceler(TenantRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void cancel(TenantId tenantId, SubscriptionId subscriptionId) {
		Tenant tenant = repository.find(tenantId).orElseThrow(() -> new ResourceNotExist("tenant", tenantId.value()));
		Tenant tenantCancelAutoRenew = tenant.cancelAutoRenew(subscriptionId);
		repository.update(tenantCancelAutoRenew);
		eventBus.publish(tenantCancelAutoRenew.pullDomainEvents());
	}
}
