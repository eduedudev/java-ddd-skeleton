package com.jaapec.tenant.subcriptions.domain;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.tenant.domain.Tenant;

public final class TenantPlanSubscription {

	private final SubscriptionId id;
	private final Tenant tenant;
	private final Plan plan;
	private final SubscriptionStatus status;
	private final SubscriptionDateSubscribed dateSubscribed;
	private final SubscriptionInitDate initDate;
	private final SubscriptionExpirationDate expirationDate;
	private final SubscriptionPricing pricing;
	private final SubscriptionPaymentStatus paymentStatus;
	private final SubscriptionCreateAt createAt;
	private final SubscriptionUpdateAt updateAt;

	public TenantPlanSubscription(
		SubscriptionId id,
		Tenant tenant,
		Plan plan,
		SubscriptionStatus status,
		SubscriptionDateSubscribed dateSubscribed,
		SubscriptionInitDate initDate,
		SubscriptionExpirationDate expirationDate,
		SubscriptionPricing pricing,
		SubscriptionPaymentStatus paymentStatus,
		SubscriptionCreateAt createAt,
		SubscriptionUpdateAt updateAt
	) {
		this.id = id;
		this.tenant = tenant;
		this.plan = plan;
		this.status = status;
		this.dateSubscribed = dateSubscribed;
		this.initDate = initDate;
		this.expirationDate = expirationDate;
		this.pricing = pricing;
		this.paymentStatus = paymentStatus;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		TenantPlanSubscription that = (TenantPlanSubscription) o;
		return id.equals(that.id) && tenant.equals(that.tenant) && plan.equals(that.plan) && status.equals(that.status);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + tenant.hashCode();
		result = 31 * result + plan.hashCode();
		result = 31 * result + status.hashCode();
		return result;
	}
}
