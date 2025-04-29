package com.jaapec.tenant.tenant.application.change_domain;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.tenant.domain.TenantDomain;
import com.jaapec.tenant.tenant.domain.TenantId;

@Service
public final class ChangeTenantDomainCommandHandler implements CommandHandler<ChangeTenantDomainCommand> {

	private final TenantDomainChanger changer;

	public ChangeTenantDomainCommandHandler(TenantDomainChanger changer) {
		this.changer = changer;
	}

	@Override
	public void handle(ChangeTenantDomainCommand command) {
		changer.change(new TenantId(command.id()), new TenantDomain(command.domain()));
	}
}
