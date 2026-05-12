package com.jaapec.tenant.tenant.domain;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.value_objects.BillingInterval;
import com.jaapec.tenant.plans.domain.value_objects.Currency;
import com.jaapec.tenant.shared.domain.AggregateRoot;
import com.jaapec.tenant.shared.domain.DateUtils;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.domain.events.*;

public final class Tenant extends AggregateRoot {

	private final TenantId id;
	private final TenantName name;
	private final List<TenantPlanSubscription> subscriptions;
	private final SubscriptionId activeSubscriptionId;
	private final TenantStatus status;
	private final TenantDomain domain;
	private final TenantDomainVerified domainVerified;
	private final TenantOwnerId ownerId;
	private final TenantCreatedAt createdAt;
	private final TenantUpdatedAt updatedAt;
	private static final int GRACE_PERIOD_DAYS_TO_SUBSCRIPTION = 5;

	public Tenant(
		TenantId id,
		TenantName name,
		TenantStatus status,
		List<TenantPlanSubscription> subscriptions,
		SubscriptionId activeSubscriptionId,
		TenantDomain domain,
		TenantDomainVerified domainVerified,
		TenantOwnerId ownerId,
		TenantCreatedAt createdAt,
		TenantUpdatedAt updatedAt
	) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.subscriptions = subscriptions;
		this.activeSubscriptionId = activeSubscriptionId;
		this.domain = domain;
		this.domainVerified = domainVerified;
		this.ownerId = ownerId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	Tenant() {
		this.id = null;
		this.name = null;
		this.status = null;
		this.subscriptions = null;
		this.activeSubscriptionId = null;
		this.domain = null;
		this.domainVerified = null;
		this.ownerId = null;
		this.createdAt = null;
		this.updatedAt = null;
	}

	public TenantId id() {
		return id;
	}

	public TenantName name() {
		return name;
	}

	public TenantStatus status() {
		return status;
	}

	public List<TenantPlanSubscription> subscriptions() {
		return subscriptions;
	}

	public SubscriptionId activeSubscriptionId() {
		return activeSubscriptionId;
	}

	public TenantDomain domain() {
		return domain;
	}

	public TenantDomainVerified domainVerified() {
		return domainVerified;
	}

	public TenantOwnerId ownerId() {
		return ownerId;
	}

	public TenantCreatedAt createdAt() {
		return createdAt;
	}

	public TenantUpdatedAt updatedAt() {
		return updatedAt;
	}

	public static Tenant create(TenantId id, TenantName name, TenantOwnerId ownerId) {
		String now = DateUtils.nowAsString();
		Tenant tenant = new Tenant(
			id,
			name,
			new TenantStatus(TenantStatus.Status.PENDING.toString()),
			null,
			null,
			null,
			null,
			ownerId,
			new TenantCreatedAt(now),
			new TenantUpdatedAt(now)
		);
		tenant.record(
			new TenantCreatedDomainEvent(
				id.value(),
				name.value(),
				ownerId.value(),
				tenant.createdAt.value(),
				tenant.updatedAt.value()
			)
		);
		return tenant;
	}

	public Tenant update(TenantName name) {
		String now = DateUtils.nowAsString();
		Tenant tenant = new Tenant(
			this.id,
			name,
			this.status,
			this.subscriptions,
			this.activeSubscriptionId,
			this.domain,
			this.domainVerified,
			this.ownerId,
			this.createdAt,
			new TenantUpdatedAt(now)
		);
		tenant.record(new TenantUpdatedDomainEvent(tenant.id().value(), tenant.name().value(), tenant.updatedAt().value()));
		return tenant;
	}

	public Tenant changeDomain(TenantDomain domain) {
		String now = DateUtils.nowAsString();
		Tenant tenant = new Tenant(
			this.id,
			this.name,
			this.status,
			this.subscriptions,
			this.activeSubscriptionId,
			domain,
			new TenantDomainVerified(false),
			this.ownerId,
			this.createdAt,
			new TenantUpdatedAt(now)
		);
		tenant.record(
			new TenantDomainChangedEvent(tenant.id().value(), tenant.domain().value(), tenant.updatedAt().value())
		);
		return tenant;
	}

	public Tenant changeStatusDomainVerified(TenantDomainVerified domainVerified) {
		String now = DateUtils.nowAsString();
		Tenant tenant = new Tenant(
			this.id,
			this.name,
			this.status,
			this.subscriptions,
			this.activeSubscriptionId,
			this.domain,
			domainVerified,
			this.ownerId,
			this.createdAt,
			new TenantUpdatedAt(now)
		);
		tenant.record(
			new TenantChangedStatusDomainEvent(tenant.id().value(), tenant.domain().value(), tenant.updatedAt().value())
		);
		return tenant;
	}

	public Tenant subscribeToPlan(
		SubscriptionId subscriptionId,
		Plan plan,
		BillingInterval interval,
		SubscriptionPricing pricing,
		Currency currency,
		SubscriptionCoupon coupon,
		SubscriptionSource source,
		SubscriptionAutoRenew autoRenew
	) {
		String now = DateUtils.nowAsString();
		List<TenantPlanSubscription> currentSubs = Optional.ofNullable(this.subscriptions).orElse(List.of());

		if (currentSubs.stream().anyMatch(TenantPlanSubscription::isActive)) {
			throw new ActiveSubscriptionAlreadyExistsException();
		}
		if (currentSubs.stream().anyMatch(s -> s.paymentStatus().isPending())) {
			throw new PendingSubscriptionExistsException();
		}

		TenantPlanSubscription subscription = TenantPlanSubscription.create(
			subscriptionId, this, plan, interval, pricing, currency, coupon, source, autoRenew
		);

		List<TenantPlanSubscription> updatedSubscriptions = Stream
			.concat(currentSubs.stream(), Stream.of(subscription))
			.toList();

		Tenant tenantWithSubscription = new Tenant(
			this.id, this.name, this.status, updatedSubscriptions, this.activeSubscriptionId,
			this.domain, this.domainVerified, this.ownerId, this.createdAt, new TenantUpdatedAt(now)
		);
		tenantWithSubscription.record(
			new TenantSubscribeToPlanEvent(
				Objects.requireNonNull(this.id).value(),
				subscriptionId.value(),
				plan.id().value(),
				interval.value(),
				pricing.value(),
				currency.value()
			)
		);
		return tenantWithSubscription;
	}

	public Tenant activateSubscription(
		SubscriptionId subscriptionId,
		SubscriptionPaymentMethod paymentMethod,
		SubscriptionPaymentReference paymentReference
	) {
		LocalDateTime now = LocalDateTime.now();

		TenantPlanSubscription latestSubscription = Optional
			.ofNullable(this.subscriptions)
			.orElse(List.of())
			.stream()
			.filter(s -> s.status().isActive() || s.status().isExpired())
			.max(Comparator.comparing(s -> s.expirationDate().valueAsDateTime()))
			.orElse(null);

		SubscriptionInitDate startDate;

		if (latestSubscription != null) {
			LocalDateTime expirationDateTime = latestSubscription.expirationDate().valueAsDateTime();
			if (!now.isAfter(expirationDateTime.plusDays(GRACE_PERIOD_DAYS_TO_SUBSCRIPTION))) {
				startDate = new SubscriptionInitDate(DateUtils.format(expirationDateTime));
			} else {
				startDate = new SubscriptionInitDate(DateUtils.format(now));
			}
		} else {
			startDate = new SubscriptionInitDate(DateUtils.format(now));
		}

		List<TenantPlanSubscription> updatedSubscriptions = Optional
			.ofNullable(this.subscriptions)
			.orElseThrow()
			.stream()
			.map(subscription -> {
				if (subscription.id().equals(subscriptionId)) {
					if (subscription.isActive()) throw new SubscriptionAlreadyActive();
					return subscription.makePayment(paymentMethod, paymentReference, startDate);
				}
				return subscription;
			})
			.toList();

		return new Tenant(
			this.id, this.name, this.status, updatedSubscriptions, subscriptionId,
			this.domain, this.domainVerified, this.ownerId, this.createdAt,
			new TenantUpdatedAt(DateUtils.nowAsString())
		);
	}

	public Tenant cancelAutoRenew(SubscriptionId subscriptionId) {
		List<TenantPlanSubscription> updatedSubscriptions = Optional
			.ofNullable(this.subscriptions)
			.orElseThrow(SubscriptionNotFound::new)
			.stream()
			.map(subscription -> {
				if (subscription.id().equals(subscriptionId)) {
					return subscription.cancelAutoRenew();
				}
				return subscription;
			})
			.toList();

		TenantPlanSubscription modifiedSubscription = updatedSubscriptions
			.stream()
			.filter(subscription -> subscription.id().equals(subscriptionId))
			.findFirst()
			.orElseThrow(SubscriptionNotFound::new);

		Tenant tenant = new Tenant(
			this.id,
			this.name,
			this.status,
			updatedSubscriptions,
			this.activeSubscriptionId,
			this.domain,
			this.domainVerified,
			this.ownerId,
			this.createdAt,
			new TenantUpdatedAt(DateUtils.nowAsString())
		);
		tenant.record(
			new TenantSubscriptionAutoRenewCanceledEvent(
				Objects.requireNonNull(this.id).value(),
				modifiedSubscription.id().value(),
				modifiedSubscription.plan().id().value(),
				modifiedSubscription.plan().name().value(),
				modifiedSubscription.plan().description().value()
			)
		);
		return tenant;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Tenant tenant = (Tenant) o;
		return (
			Objects.requireNonNull(id).equals(tenant.id) &&
			Objects.requireNonNull(name).equals(tenant.name) &&
			Objects.requireNonNull(status).equals(tenant.status) &&
			Objects.equals(domain, tenant.domain) &&
			Objects.equals(domainVerified, tenant.domainVerified) &&
			Objects.requireNonNull(ownerId).equals(tenant.ownerId)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.requireNonNull(id).hashCode();
		result = 31 * result + Objects.requireNonNull(name).hashCode();
		result = 31 * result + Objects.requireNonNull(status).hashCode();
		result = 31 * result + Objects.hashCode(domain);
		result = 31 * result + Objects.hashCode(domainVerified);
		result = 31 * result + Objects.requireNonNull(ownerId).hashCode();
		return result;
	}
}
