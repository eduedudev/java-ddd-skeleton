package com.jaapec.tenant.tenant.application.add_subscription;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record AddSubscriptionCommand(
	String id,
	String tenantId,
	String planId,
	String billingInterval,
	int pricing,
	String currency,
	String coupon,
	String source,
	boolean autoRenew
)
	implements Command {}
