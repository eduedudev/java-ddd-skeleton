package com.jaapec.tenant.tenant.application.find;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Service
public final class TenantFinder {

	private final TenantRepository repository;

	public TenantFinder(TenantRepository repository) {
		this.repository = repository;
	}

	public TenantResponse find(TenantId id) {
		return repository
			.find(id)
			.map(TenantResponse::fromAggregate)
			.orElseThrow(() -> new ResourceNotExist("tenant", id.value()));
	}
}
