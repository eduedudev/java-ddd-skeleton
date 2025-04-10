package com.jaapec.tenant.plans.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class PlanDeletedDomainEvent extends DomainEvent {

	public PlanDeletedDomainEvent() {
		super(null);
	}

	public PlanDeletedDomainEvent(String aggregateId) {
		super(aggregateId);
	}

	public PlanDeletedDomainEvent(String aggregateId, String eventId, String occurredOn) {
		super(aggregateId, eventId, occurredOn);
	}

	@Override
	public String eventName() {
		return "plan.deleted";
	}

	@Override
	public Map<String, Serializable> toPrimitives() {
		return new HashMap<>();
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new PlanDeletedDomainEvent(aggregateId, eventId, occurredOn);
	}
}
