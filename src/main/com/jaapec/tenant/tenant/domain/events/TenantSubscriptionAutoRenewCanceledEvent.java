package com.jaapec.tenant.tenant.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class TenantSubscriptionAutoRenewCanceledEvent extends DomainEvent {

	private final String subscriptionId;
	private final String planId;
	private final String planName;
	private final String planDescription;

	public TenantSubscriptionAutoRenewCanceledEvent() {
		super(null);
		this.subscriptionId = null;
		this.planId = null;
		this.planName = null;
		this.planDescription = null;
	}

	public TenantSubscriptionAutoRenewCanceledEvent(
		String aggregateId,
		String subscriptionId,
		String planId,
		String planName,
		String planDescription
	) {
		super(aggregateId);
		this.subscriptionId = subscriptionId;
		this.planId = planId;
		this.planName = planName;
		this.planDescription = planDescription;
	}

	@Override
	public String eventName() {
		return "subscription.auto.renew.canceled";
	}

	@Override
	public Map<String, Serializable> toPrimitives() {
		HashMap<String, Serializable> primitives = new HashMap<>();
		primitives.put("subscriptionId", subscriptionId);
		primitives.put("planId", planId);
		primitives.put("planName", planName);
		primitives.put("planDescription", planDescription);
		return primitives;
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new TenantSubscriptionAutoRenewCanceledEvent(
			aggregateId,
			(String) body.get("subscriptionId"),
			(String) body.get("planId"),
			(String) body.get("planName"),
			(String) body.get("planDescription")
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		TenantSubscriptionAutoRenewCanceledEvent that = (TenantSubscriptionAutoRenewCanceledEvent) o;
		return (
			Objects.equals(subscriptionId, that.subscriptionId) &&
			Objects.equals(planId, that.planId) &&
			Objects.equals(planName, that.planName) &&
			Objects.equals(planDescription, that.planDescription)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(subscriptionId);
		result = 31 * result + Objects.hashCode(planId);
		result = 31 * result + Objects.hashCode(planName);
		result = 31 * result + Objects.hashCode(planDescription);
		return result;
	}
}
