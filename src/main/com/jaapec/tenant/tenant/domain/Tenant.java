package com.jaapec.tenant.tenant.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.jaapec.tenant.tenant.domain.events.TenantChangedStatusDomainEvent;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;
import com.jaapec.tenant.tenant.domain.events.TenantDomainChangedEvent;
import com.jaapec.tenant.tenant.domain.events.TenantUpdatedDomainEvent;

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

	public Tenant() {
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
		TenantPlanSubscription subscription = TenantPlanSubscription.create(
			subscriptionId,
			this,
			plan,
			interval,
			pricing,
			currency,
			coupon,
			source,
			autoRenew
		);
		List<TenantPlanSubscription> currentSubs = Optional.ofNullable(this.subscriptions).orElse(List.of());
		boolean hasActiveSubscription = currentSubs.stream().anyMatch(TenantPlanSubscription::isActive);

		if (hasActiveSubscription) {
			throw new ActiveSubscriptionAlreadyExistsException();
		}

		boolean paymentPending = currentSubs.stream().anyMatch(s -> s.paymentStatus().isPending());

		if (paymentPending) {
			throw new PendingSubscriptionExistsException();
		}

		List<TenantPlanSubscription> updatedSubscriptions = Stream
			.concat(Optional.ofNullable(this.subscriptions).orElse(List.of()).stream(), Stream.of(subscription))
			.toList();

		return new Tenant(
			this.id,
			this.name,
			this.status,
			updatedSubscriptions,
			this.activeSubscriptionId,
			this.domain,
			this.domainVerified,
			this.ownerId,
			this.createdAt,
			new TenantUpdatedAt(now)
		);
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

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		if (latestSubscription != null) {
			LocalDateTime expirationDateTime = latestSubscription.expirationDate().valueAsDateTime();

			if (!now.isAfter(expirationDateTime.plusDays(GRACE_PERIOD_DAYS_TO_SUBSCRIPTION))) {
				startDate = new SubscriptionInitDate(expirationDateTime.format(formatter));
			} else {
				startDate = new SubscriptionInitDate(now.format(formatter));
			}
		} else {
			startDate = new SubscriptionInitDate(now.format(formatter));
		}

		List<TenantPlanSubscription> updatedSubscriptions = Optional
			.ofNullable(this.subscriptions)
			.orElseThrow()
			.stream()
			.map(subscription -> {
				if (subscription.id().equals(subscriptionId)) {
					if (subscription.isActive()) {
						throw new SubscriptionAlreadyActive();
					}
					return subscription.makePayment(paymentMethod, paymentReference, startDate);
				}
				return subscription;
			})
			.toList();

		return new Tenant(
			this.id,
			this.name,
			this.status,
			updatedSubscriptions,
			subscriptionId,
			this.domain,
			this.domainVerified,
			this.ownerId,
			this.createdAt,
			new TenantUpdatedAt(DateUtils.nowAsString())
		);
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
