package com.jaapec.tenant.plans.application.update;

import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;

@Service
public final class UpdatePlanCommandHandler implements CommandHandler<UpdatePlanCommand> {

	private final PlanUpdater updater;

	public UpdatePlanCommandHandler(PlanUpdater updater) {
		this.updater = updater;
	}

	@Override
	public void handle(UpdatePlanCommand command) {
		updater.update(
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
