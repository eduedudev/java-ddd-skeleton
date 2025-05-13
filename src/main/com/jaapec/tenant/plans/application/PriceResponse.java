package com.jaapec.tenant.plans.application;

public record PriceResponse(
	String id,
	String billingInterval,
	int amount,
	String currency,
	String createdAt,
	String updatedAt
) {
	public static PriceResponse fromAggregate(
		String id,
		String billingInterval,
		int amount,
		String currency,
		String createdAt,
		String updatedAt
	) {
		return new PriceResponse(id, billingInterval, amount, currency, createdAt, updatedAt);
	}
}
