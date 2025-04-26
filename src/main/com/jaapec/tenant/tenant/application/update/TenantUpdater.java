package com.jaapec.tenant.tenant.application.update;

import com.jaapec.tenant.shared.domain.ResourceAlreadyExists;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantName;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Service
public final class TenantUpdater {

	private final TenantRepository repository;
	private final EventBus eventBus;

	public TenantUpdater(TenantRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void update(TenantId id, TenantName name) {
		Tenant tenant = repository.find(id).orElseThrow(() -> new ResourceNotExist("tenant", id.value()));

		if (!tenant.name().equals(name) && repository.uniqueField("name", name.value())) {
			throw new ResourceAlreadyExists("tenant", "name", name.value());
		}

		Tenant updatedTenant = tenant.update(name);
		repository.update(updatedTenant);
		eventBus.publish(updatedTenant.pullDomainEvents());
	}
}
