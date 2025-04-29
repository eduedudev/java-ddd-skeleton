package com.jaapec.tenant.tenant.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class TenantDomainChangedEvent extends DomainEvent {

	private final String domain;
	private final String updateAt;

	public TenantDomainChangedEvent() {
		super(null);
		this.domain = null;
		this.updateAt = null;
	}

	public TenantDomainChangedEvent(String aggregateId, String domain, String updateAt) {
		super(aggregateId);
		this.domain = domain;
		this.updateAt = updateAt;
	}

	@Override
	public String eventName() {
		return "domain.changed";
	}

	@Override
	public HashMap<String, Serializable> toPrimitives() {
		HashMap<String, Serializable> primitives = new HashMap<>();
		primitives.put("domain", domain);
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
		return new TenantDomainChangedEvent(aggregateId, (String) body.get("domain"), (String) body.get("updateAt"));
	}
}
