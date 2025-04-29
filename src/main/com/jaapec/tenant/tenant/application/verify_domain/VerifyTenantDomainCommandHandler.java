package com.jaapec.tenant.tenant.application.verify_domain;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.tenant.domain.TenantId;

@Service
public final class VerifyTenantDomainCommandHandler implements CommandHandler<VerifyTenantDomainCommand> {

	private final TenantDomainVerifier verifier;

	public VerifyTenantDomainCommandHandler(TenantDomainVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	public void handle(VerifyTenantDomainCommand command) {
		verifier.verifier(new TenantId(command.tenantId()));
	}
}
