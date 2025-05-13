package com.jaapec.tenant.plans.application.create;

import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;

@Service
public final class CreatePlanCommandHandler implements CommandHandler<CreatePlanCommand> {

	private final PlanCreator creator;

	public CreatePlanCommandHandler(PlanCreator creator) {
		this.creator = creator;
	}

	@Override
	public void handle(CreatePlanCommand command) {
		creator.create(
			new PlanId(command.id()),
			new PlanName(command.name()),
			new PlanDescription(command.description()),
			new PlanMaxUsers(command.maxUsers()),
			new PlanMaxRoles(command.maxRoles()),
			new PlanMaxAccounts(command.maxAccounts()),
			new PlanMaxInvoices(command.maxInvoices()),
			new PlanStatus(command.status()),
			new PlanVisibility(command.visibility()),
			new PlanTrialDays(command.trialDays())
		);
	}
}
