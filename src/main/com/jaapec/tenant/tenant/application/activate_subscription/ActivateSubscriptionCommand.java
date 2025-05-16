package com.jaapec.tenant.tenant.application.activate_subscription;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record ActivateSubscriptionCommand(
	String tenantId,
	String subscriptionId,
	String paymentMethod,
	String paymentReference
)
	implements Command {}
