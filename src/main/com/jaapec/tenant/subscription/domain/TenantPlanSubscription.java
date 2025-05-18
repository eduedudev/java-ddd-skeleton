package com.jaapec.tenant.subscription.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.value_objects.BillingInterval;
import com.jaapec.tenant.plans.domain.value_objects.Currency;
import com.jaapec.tenant.shared.domain.DateUtils;
import com.jaapec.tenant.tenant.domain.Tenant;

public final class TenantPlanSubscription {

	private final SubscriptionId id;
	private final Tenant tenant;
	private final Plan plan;
	private final BillingInterval billingInterval;
	private final SubscriptionStatus status;
	private final SubscriptionDateSubscribed dateSubscribed;
	private final SubscriptionInitDate initDate;
	private final SubscriptionExpirationDate expirationDate;
	private final SubscriptionPricing pricing;
	private final Currency currency;
	private final SubscriptionCoupon coupon;
	private final SubscriptionSource source;
	private final SubscriptionPaymentStatus paymentStatus;
	private final SubscriptionPaymentMethod paymentMethod;
	private final SubscriptionPaymentReference paymentReference;
	private final SubscriptionAutoRenew autoRenew;
	private final SubscriptionCreateAt createdAt;
	private final SubscriptionUpdateAt updatedAt;

	public TenantPlanSubscription(
		SubscriptionId id,
		Tenant tenant,
		Plan plan,
		BillingInterval billingInterval,
		SubscriptionStatus status,
		SubscriptionDateSubscribed dateSubscribed,
		SubscriptionInitDate initDate,
		SubscriptionExpirationDate expirationDate,
		SubscriptionPricing pricing,
		Currency currency,
		SubscriptionCoupon coupon,
		SubscriptionSource source,
		SubscriptionPaymentStatus paymentStatus,
		SubscriptionPaymentMethod paymentMethod,
		SubscriptionPaymentReference paymentReference,
		SubscriptionAutoRenew autoRenew,
		SubscriptionCreateAt createdAt,
		SubscriptionUpdateAt updatedAt
	) {
		this.id = id;
		this.tenant = tenant;
		this.plan = plan;
		this.billingInterval = billingInterval;
		this.status = status;
		this.dateSubscribed = dateSubscribed;
		this.initDate = initDate;
		this.expirationDate = expirationDate;
		this.pricing = pricing;
		this.currency = currency;
		this.coupon = coupon;
		this.source = source;
		this.paymentStatus = paymentStatus;
		this.paymentMethod = paymentMethod;
		this.paymentReference = paymentReference;
		this.autoRenew = autoRenew;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public TenantPlanSubscription() {
		this.id = null;
		this.tenant = null;
		this.plan = null;
		this.billingInterval = null;
		this.status = null;
		this.dateSubscribed = null;
		this.initDate = null;
		this.expirationDate = null;
		this.pricing = null;
		this.currency = null;
		this.coupon = null;
		this.source = null;
		this.paymentStatus = null;
		this.paymentMethod = null;
		this.paymentReference = null;
		this.autoRenew = null;
		this.createdAt = null;
		this.updatedAt = null;
	}

	public SubscriptionId id() {
		return id;
	}

	public Tenant tenant() {
		return tenant;
	}

	public Plan plan() {
		return plan;
	}

	public BillingInterval billingInterval() {
		return billingInterval;
	}

	public SubscriptionStatus status() {
		return status;
	}

	public SubscriptionDateSubscribed dateSubscribed() {
		return dateSubscribed;
	}

	public SubscriptionInitDate initDate() {
		return initDate;
	}

	public SubscriptionExpirationDate expirationDate() {
		return expirationDate;
	}

	public SubscriptionPricing pricing() {
		return pricing;
	}

	public Currency currency() {
		return currency;
	}

	public SubscriptionCoupon coupon() {
		return coupon;
	}

	public SubscriptionSource source() {
		return source;
	}

	public SubscriptionPaymentStatus paymentStatus() {
		return paymentStatus;
	}

	public SubscriptionPaymentMethod paymentMethod() {
		return paymentMethod;
	}

	public SubscriptionPaymentReference paymentReference() {
		return paymentReference;
	}

	public SubscriptionAutoRenew autoRenew() {
		return autoRenew;
	}

	public SubscriptionCreateAt createdAt() {
		return createdAt;
	}

	public SubscriptionUpdateAt updatedAt() {
		return updatedAt;
	}

	public static TenantPlanSubscription create(
		SubscriptionId subscriptionId,
		Tenant tenant,
		Plan plan,
		BillingInterval interval,
		SubscriptionPricing pricing,
		Currency currency,
		SubscriptionCoupon coupon,
		SubscriptionSource source,
		SubscriptionAutoRenew autoRenew
	) {
		String now = DateUtils.nowAsString();
		return new TenantPlanSubscription(
			subscriptionId,
			tenant,
			plan,
			interval,
			new SubscriptionStatus(SubscriptionStatus.status.INACTIVE.name()),
			null,
			null,
			null,
			pricing,
			currency,
			coupon,
			source,
			new SubscriptionPaymentStatus(SubscriptionPaymentStatus.status.PENDING.name()),
			null,
			null,
			autoRenew,
			new SubscriptionCreateAt(now),
			new SubscriptionUpdateAt(now)
		);
	}

	public TenantPlanSubscription makePayment(
		SubscriptionPaymentMethod paymentMethod,
		SubscriptionPaymentReference paymentReference,
		SubscriptionInitDate startDate
	) {
		String now = DateUtils.nowAsString();
		LocalDateTime startDateTime = startDate.valueAsDateTime();

		LocalDateTime expirationDateTime = BillingIntervalHelper.calculateExpiration(
			startDateTime,
			Objects.requireNonNull(this.billingInterval)
		);
		String formattedDateTime = expirationDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return new TenantPlanSubscription(
			this.id,
			this.tenant,
			this.plan,
			this.billingInterval,
			new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name()),
			new SubscriptionDateSubscribed(now),
			startDate,
			new SubscriptionExpirationDate(formattedDateTime),
			this.pricing,
			this.currency,
			this.coupon,
			this.source,
			new SubscriptionPaymentStatus(SubscriptionPaymentStatus.status.PAID.toString()),
			paymentMethod,
			paymentReference,
			this.autoRenew,
			this.createdAt,
			new SubscriptionUpdateAt(now)
		);
	}

	public TenantPlanSubscription cancelAutoRenew() {
		if (!this.isActive()) throw new SubscriptionIsInactiveException();
		String now = DateUtils.nowAsString();
		return new TenantPlanSubscription(
			this.id,
			this.tenant,
			this.plan,
			this.billingInterval,
			this.status,
			this.dateSubscribed,
			this.initDate,
			this.expirationDate,
			this.pricing,
			this.currency,
			this.coupon,
			this.source,
			this.paymentStatus,
			this.paymentMethod,
			this.paymentReference,
			new SubscriptionAutoRenew(false),
			this.createdAt,
			new SubscriptionUpdateAt(now)
		);
	}

	private static class BillingIntervalHelper {

		public static LocalDateTime calculateExpiration(LocalDateTime initDateTime, BillingInterval interval) {
			return switch (interval.value()) {
				case "MONTHLY" -> initDateTime.plusMonths(1);
				case "YEARLY" -> initDateTime.plusYears(1);
				default -> throw new IllegalArgumentException("Unsupported interval: " + interval.value());
			};
		}
	}

	public boolean isActive() {
		return Objects.requireNonNull(this.status).value().equals(SubscriptionStatus.status.ACTIVE.name());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		TenantPlanSubscription that = (TenantPlanSubscription) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
