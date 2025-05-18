package com.jaapec.tenant.tenant.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class TenantSubscribeToPlanEvent extends DomainEvent {

	private final String subscriptionId;
	private final String planId;
	private final String interval;
	private final int pricing;
	private final String currency;

	public TenantSubscribeToPlanEvent() {
		super(null);
		this.subscriptionId = null;
		this.planId = null;
		this.interval = null;
		this.pricing = 0;
		this.currency = null;
	}

	public TenantSubscribeToPlanEvent(
		String aggregateId,
		String subscriptionId,
		String planId,
		String interval,
		int pricing,
		String currency
	) {
		super(aggregateId);
		this.subscriptionId = subscriptionId;
		this.planId = planId;
		this.interval = interval;
		this.pricing = pricing;
		this.currency = currency;
	}

	@Override
	public String eventName() {
		return "tenant.subscribe.to.plan";
	}

	@Override
	public Map<String, Serializable> toPrimitives() {
		HashMap<String, Serializable> primitives = new HashMap<>();
		primitives.put("subscriptionId", subscriptionId);
		primitives.put("planId", planId);
		primitives.put("interval", interval);
		primitives.put("pricing", pricing);
		primitives.put("currency", currency);
		return primitives;
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new TenantSubscribeToPlanEvent(
			aggregateId,
			(String) body.get("subscriptionId"),
			(String) body.get("planId"),
			(String) body.get("interval"),
			(int) body.get("pricing"),
			(String) body.get("currency")
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		TenantSubscribeToPlanEvent that = (TenantSubscribeToPlanEvent) o;
		return (
			pricing == that.pricing &&
			Objects.equals(subscriptionId, that.subscriptionId) &&
			Objects.equals(planId, that.planId) &&
			Objects.equals(interval, that.interval) &&
			Objects.equals(currency, that.currency)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(subscriptionId);
		result = 31 * result + Objects.hashCode(planId);
		result = 31 * result + Objects.hashCode(interval);
		result = 31 * result + pricing;
		result = 31 * result + Objects.hashCode(currency);
		return result;
	}
}
