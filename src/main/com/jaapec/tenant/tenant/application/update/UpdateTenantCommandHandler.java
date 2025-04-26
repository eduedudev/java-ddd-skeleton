package com.jaapec.tenant.tenant.application.update;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantName;

@Service
public final class UpdateTenantCommandHandler implements CommandHandler<UpdateTenantCommand> {

	private final TenantUpdater updater;

	public UpdateTenantCommandHandler(TenantUpdater updater) {
		this.updater = updater;
	}

	@Override
	public void handle(UpdateTenantCommand command) {
		updater.update(new TenantId(command.id()), new TenantName(command.name()));
	}
}
