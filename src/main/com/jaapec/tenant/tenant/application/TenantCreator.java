package com.jaapec.tenant.tenant.application;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.tenant.domain.*;

@Service
public final class TenantCreator {

	private final TenantRepository repository;
	private final EventBus eventBus;

	public TenantCreator(TenantRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void create(TenantId id, TenantName name, TenantOwnerId ownerId) {
		Tenant tenant = Tenant.create(id, name, ownerId);

		repository.save(tenant);
		eventBus.publish(tenant.pullDomainEvents());
	}
}
