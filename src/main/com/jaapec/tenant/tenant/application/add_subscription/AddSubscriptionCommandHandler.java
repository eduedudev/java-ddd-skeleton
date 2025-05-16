package com.jaapec.tenant.tenant.application.add_subscription;

import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.domain.TenantId;

@Service
public final class AddSubscriptionCommandHandler implements CommandHandler<AddSubscriptionCommand> {

	private final SubscriptionAdder adder;

	public AddSubscriptionCommandHandler(SubscriptionAdder adder) {
		this.adder = adder;
	}

	@Override
	public void handle(AddSubscriptionCommand command) {
		adder.addSubscription(
			new SubscriptionId(command.id()),
			new TenantId(command.tenantId()),
			new PlanId(command.planId()),
			new BillingInterval(command.billingInterval()),
			new SubscriptionPricing(command.pricing()),
			new Currency(command.currency()),
			new SubscriptionCoupon(command.coupon()),
			new SubscriptionSource(command.source()),
			new SubscriptionAutoRenew(command.autoRenew())
		);
	}
}
