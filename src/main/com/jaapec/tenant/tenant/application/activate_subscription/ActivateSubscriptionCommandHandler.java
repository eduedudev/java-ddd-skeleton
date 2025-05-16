package com.jaapec.tenant.tenant.application.activate_subscription;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.subscription.domain.SubscriptionId;
import com.jaapec.tenant.subscription.domain.SubscriptionPaymentMethod;
import com.jaapec.tenant.subscription.domain.SubscriptionPaymentReference;
import com.jaapec.tenant.tenant.domain.TenantId;

@Service
public final class ActivateSubscriptionCommandHandler implements CommandHandler<ActivateSubscriptionCommand> {

	private final SubscriptionActivator activator;

	public ActivateSubscriptionCommandHandler(SubscriptionActivator activator) {
		this.activator = activator;
	}

	@Override
	public void handle(ActivateSubscriptionCommand command) {
		activator.activate(
			new TenantId(command.tenantId()),
			new SubscriptionId(command.subscriptionId()),
			new SubscriptionPaymentMethod(command.paymentMethod()),
			new SubscriptionPaymentReference(command.paymentReference())
		);
	}
}
