package com.jaapec.tenant.tenant.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class TenantChangedStatusDomainEvent extends DomainEvent {

	private final String domainVerified;
	private final String updateAt;

	public TenantChangedStatusDomainEvent() {
		super(null);
		this.domainVerified = null;
		this.updateAt = null;
	}

	public TenantChangedStatusDomainEvent(String aggregateId, String domainVerified, String updateAt) {
		super(aggregateId);
		this.domainVerified = domainVerified;
		this.updateAt = updateAt;
	}

	@Override
	public String eventName() {
		return "domain.verified";
	}

	@Override
	public HashMap<String, Serializable> toPrimitives() {
		HashMap<String, Serializable> primitives = new HashMap<>();
		primitives.put("domainVerified", domainVerified);
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
		return new TenantChangedStatusDomainEvent(
			aggregateId,
			(String) body.get("domainVerified"),
			(String) body.get("updateAt")
		);
	}
}
