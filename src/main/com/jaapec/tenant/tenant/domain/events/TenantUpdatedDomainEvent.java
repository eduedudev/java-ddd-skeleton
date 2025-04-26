package com.jaapec.tenant.tenant.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class TenantUpdatedDomainEvent extends DomainEvent {

	private final String name;
	private final String updateAt;

	public TenantUpdatedDomainEvent() {
		super(null);
		this.name = null;
		this.updateAt = null;
	}

	public TenantUpdatedDomainEvent(String aggregateId, String name, String updateAt) {
		super(aggregateId);
		this.name = name;
		this.updateAt = updateAt;
	}

	@Override
	public String eventName() {
		return "tenant.updated";
	}

	@Override
	public HashMap<String, Serializable> toPrimitives() {
		HashMap<String, Serializable> primitives = new HashMap<>();
		primitives.put("name", name);
		primitives.put("updateAt", updateAt);
		return primitives;
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new TenantUpdatedDomainEvent(aggregateId, (String) body.get("name"), (String) body.get("updateAt"));
	}
}
