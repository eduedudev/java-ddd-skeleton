package com.jaapec.tenant.tenant.application.verify_domain;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.tenant.domain.*;

@Service
public final class TenantDomainVerifier {

	private final TenantRepository repository;
	private final EventBus eventBus;
	private final DomainVerificationChecker checker;

	public TenantDomainVerifier(TenantRepository repository, EventBus eventBus, DomainVerificationChecker checker) {
		this.repository = repository;
		this.eventBus = eventBus;
		this.checker = checker;
	}

	public void verifier(TenantId id) {
		Tenant tenant = repository.find(id).orElseThrow(() -> new ResourceNotExist("tenant", id.value()));
		boolean isVerified = checker.isVerified(tenant);
		TenantDomainVerified status = new TenantDomainVerified(isVerified);
		Tenant updatedTenant = tenant.changeStatusDomainVerified(status);
		repository.update(updatedTenant);
		eventBus.publish(updatedTenant.pullDomainEvents());
	}
}
