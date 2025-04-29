package com.jaapec.tenant.tenant.application.change_domain;

import java.util.Objects;

import com.jaapec.tenant.shared.domain.DuplicateFieldException;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.tenant.domain.*;

@Service
public final class TenantDomainChanger {

	private final TenantRepository repository;
	private final EventBus eventBus;

	public TenantDomainChanger(TenantRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void change(TenantId id, TenantDomain domain) {
		Tenant tenant = repository.find(id).orElseThrow(() -> new ResourceNotExist("tenant", id.value()));

		if (!Objects.equals(tenant.domain(), domain) && repository.uniqueField("domain", domain.value())) {
			throw new DuplicateFieldException("domain", domain.value());
		}

		Tenant tenantDomainChanged = tenant.changeDomain(domain);
		repository.update(tenantDomainChanged);
		eventBus.publish(tenantDomainChanged.pullDomainEvents());
	}
}
