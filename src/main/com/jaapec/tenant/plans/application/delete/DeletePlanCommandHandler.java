package com.jaapec.tenant.plans.application.delete;

import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;

@Service
public final class DeletePlanCommandHandler implements CommandHandler<DeletePlanCommand> {

	private final PlanDeleter deleter;

	public DeletePlanCommandHandler(PlanDeleter deleter) {
		this.deleter = deleter;
	}

	@Override
	public void handle(DeletePlanCommand command) {
		deleter.delete(new PlanId(command.id()));
	}
}
