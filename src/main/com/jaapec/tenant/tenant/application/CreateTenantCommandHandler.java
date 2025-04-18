package com.jaapec.tenant.tenant.application;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantName;
import com.jaapec.tenant.tenant.domain.TenantOwnerId;

@Service
public final class CreateTenantCommandHandler implements CommandHandler<CreateTenantCommand> {

	private final TenantCreator creator;

	public CreateTenantCommandHandler(TenantCreator creator) {
		this.creator = creator;
	}

	@Override
	public void handle(CreateTenantCommand command) {
		creator.create(new TenantId(command.id()), new TenantName(command.name()), new TenantOwnerId(command.ownerId()));
	}
}
