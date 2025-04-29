package com.jaapec.tenant.tenant.application.check_domain_verification;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.tenant.application.verify_domain.VerifyTenantDomainCommand;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Service
public final class DomainVerifier {

	private final TenantRepository repository;
	private final CommandBus commandBus;

	public DomainVerifier(TenantRepository repository, CommandBus commandBus) {
		this.repository = repository;
		this.commandBus = commandBus;
	}

	public DomainVerificationResponse verify(TenantId tenantId) {
		commandBus.dispatch(new VerifyTenantDomainCommand(tenantId.value()));
		return repository
			.find(tenantId)
			.map(DomainVerificationResponse::fromAggregate)
			.orElseThrow(() -> new ResourceNotExist("tenant", tenantId.value()));
	}
}
