package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

public record RequestSubscription(
	String planId,
	String billingInterval,
	int pricing,
	String currency,
	String coupon,
	String source,
	boolean autoRenew
) {}
