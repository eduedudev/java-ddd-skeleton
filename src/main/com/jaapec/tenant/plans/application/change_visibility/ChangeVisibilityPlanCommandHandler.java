package com.jaapec.tenant.plans.application.change_visibility;

import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;

@Service
public final class ChangeVisibilityPlanCommandHandler implements CommandHandler<ChangeVisibilityPlanCommand> {

	private final ChangeVisibility visibility;

	public ChangeVisibilityPlanCommandHandler(ChangeVisibility visibility) {
		this.visibility = visibility;
	}

	@Override
	public void handle(ChangeVisibilityPlanCommand command) {
		visibility.changeVisibility(new PlanId(command.id()), new PlanVisibility(command.visibility()));
	}
}
