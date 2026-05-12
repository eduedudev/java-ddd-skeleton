package com.jaapec.tenant.plans.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class ChangeVisibilityPlanDomainEvent extends DomainEvent {

	private final String visibility;
	private final String updatedAt;

	public ChangeVisibilityPlanDomainEvent(String visibility, String updatedAt) {
		super(null);
		this.visibility = visibility;
		this.updatedAt = updatedAt;
	}

	public ChangeVisibilityPlanDomainEvent(String aggregateId, String visibility, String updatedAt) {
		super(aggregateId);
		this.visibility = visibility;
		this.updatedAt = updatedAt;
	}

	public ChangeVisibilityPlanDomainEvent(
		String aggregateId,
		String eventId,
		String occurredOn,
		String visibility,
		String updatedAt
	) {
		super(aggregateId, eventId, occurredOn);
		this.visibility = visibility;
		this.updatedAt = updatedAt;
	}

	ChangeVisibilityPlanDomainEvent() {
		super(null);
		this.visibility = null;
		this.updatedAt = null;
	}

	@Override
	public String eventName() {
		return "plan.change_visibility";
	}

	@Override
	public Map<String, Serializable> toPrimitives() {
		Map<String, Serializable> primitives = new HashMap<>();
		primitives.put("visibility", visibility);
		primitives.put("updatedAt", updatedAt);
		return primitives;
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new ChangeVisibilityPlanDomainEvent(
			aggregateId,
			eventId,
			occurredOn,
			(String) body.get("visibility"),
			(String) body.get("updatedAt")
		);
	}
}
