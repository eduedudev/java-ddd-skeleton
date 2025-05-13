package com.jaapec.tenant.plans.application.add_price;

import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;

@Service
public final class AddPlanPriceCommandHandler implements CommandHandler<AddPlanPriceCommand> {

	private final PlanPriceAdder adder;

	public AddPlanPriceCommandHandler(PlanPriceAdder adder) {
		this.adder = adder;
	}

	@Override
	public void handle(AddPlanPriceCommand command) {
		adder.priceAdder(
			new PlanId(command.planId()),
			new PlanPriceId(command.id()),
			new BillingInterval(command.billingInterval()),
			new Amount(command.amount()),
			new Currency(command.currency())
		);
	}
}
