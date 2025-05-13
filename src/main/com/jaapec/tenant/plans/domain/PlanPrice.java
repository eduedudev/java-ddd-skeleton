package com.jaapec.tenant.plans.domain;

import java.util.Objects;

import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.CurrentDate;

public final class PlanPrice {

	private final PlanPriceId id;
	private final BillingInterval billingInterval;
	private final Amount amount;
	private final Currency currency;
	private final Plan plan;
	private final PlanPriceCreatedAt createdAt;
	private final PlanPriceUpdatedAt updatedAt;

	public PlanPrice(
		PlanPriceId id,
		BillingInterval billingInterval,
		Amount amount,
		Currency currency,
		Plan plan,
		PlanPriceCreatedAt createdAt,
		PlanPriceUpdatedAt updatedAt
	) {
		this.id = id;
		this.billingInterval = billingInterval;
		this.amount = amount;
		this.currency = currency;
		this.plan = plan;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public PlanPrice() {
		this.id = null;
		this.billingInterval = null;
		this.amount = null;
		this.currency = null;
		this.plan = null;
		this.createdAt = null;
		this.updatedAt = null;
	}

	public PlanPriceId id() {
		return id;
	}

	public BillingInterval billingInterval() {
		return billingInterval;
	}

	public Amount amount() {
		return amount;
	}

	public Currency currency() {
		return currency;
	}

	public PlanPriceCreatedAt createdAt() {
		return createdAt;
	}

	public PlanPriceUpdatedAt updatedAt() {
		return updatedAt;
	}

	public Plan plan() {
		return plan;
	}

	public static PlanPrice create(PlanPriceId id, BillingInterval billingInterval, Amount amount, Currency currency) {
		String now = CurrentDate.now();
		return new PlanPrice(
			id,
			billingInterval,
			amount,
			currency,
			null,
			new PlanPriceCreatedAt(now),
			new PlanPriceUpdatedAt(now)
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		PlanPrice planPrice = (PlanPrice) o;
		return (
			Objects.equals(id, planPrice.id) &&
			Objects.equals(billingInterval, planPrice.billingInterval) &&
			Objects.equals(amount, planPrice.amount) &&
			Objects.equals(currency, planPrice.currency) &&
			Objects.equals(plan, planPrice.plan) &&
			Objects.equals(createdAt, planPrice.createdAt) &&
			Objects.equals(updatedAt, planPrice.updatedAt)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(billingInterval);
		result = 31 * result + Objects.hashCode(amount);
		result = 31 * result + Objects.hashCode(currency);
		result = 31 * result + Objects.hashCode(plan);
		result = 31 * result + Objects.hashCode(createdAt);
		result = 31 * result + Objects.hashCode(updatedAt);
		return result;
	}
}
