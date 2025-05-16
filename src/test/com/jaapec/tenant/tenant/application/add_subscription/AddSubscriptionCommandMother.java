package com.jaapec.tenant.tenant.application.add_subscription;

import java.util.UUID;

import com.jaapec.tenant.plan.domain.BillingIntervalMother;
import com.jaapec.tenant.plan.domain.CurrencyMother;
import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plans.domain.value_objects.BillingInterval;
import com.jaapec.tenant.plans.domain.value_objects.Currency;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.domain.TenantId;

public final class AddSubscriptionCommandMother {

	public static AddSubscriptionCommand create(
		String id,
		String tenantId,
		String planId,
		String billingInterval,
		int pricing,
		String currency,
		String coupon,
		String source,
		boolean autoRenew
	) {
		return new AddSubscriptionCommand(
			id,
			tenantId,
			planId,
			billingInterval,
			pricing,
			currency,
			coupon,
			source,
			autoRenew
		);
	}

	public static AddSubscriptionCommand create(
		SubscriptionId id,
		TenantId tenantId,
		com.jaapec.tenant.plans.domain.value_objects.PlanId planId,
		BillingInterval billingInterval,
		SubscriptionPricing pricing,
		Currency currency,
		SubscriptionCoupon coupon,
		SubscriptionSource source,
		SubscriptionAutoRenew autoRenew
	) {
		return new AddSubscriptionCommand(
			id.value(),
			tenantId.value(),
			planId.value(),
			billingInterval.value(),
			pricing.value(),
			currency.value(),
			coupon.value(),
			source.value(),
			autoRenew.value()
		);
	}

	public static AddSubscriptionCommand random(TenantId tenantId) {
		return create(
			UUID.randomUUID().toString(),
			tenantId.value(),
			PlanIdMother.random().value(),
			BillingIntervalMother.random().value(),
			100,
			CurrencyMother.random().value(),
			"COUPON123",
			"web",
			true
		);
	}
}
