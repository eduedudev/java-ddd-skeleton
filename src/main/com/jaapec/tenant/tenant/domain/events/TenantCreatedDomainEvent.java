package com.jaapec.tenant.tenant.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class TenantCreatedDomainEvent extends DomainEvent {

	private final String name;
	private final String ownerId;
	private final String createAt;
	private final String updateAt;

	TenantCreatedDomainEvent() {
		super(null);
		this.name = null;
		this.ownerId = null;
		this.createAt = null;
		this.updateAt = null;
	}

	public TenantCreatedDomainEvent(String aggregateId, String name, String ownerId, String createAt, String updateAt) {
		super(aggregateId);
		this.name = name;
		this.ownerId = ownerId;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}

	@Override
	public String eventName() {
		return "tenant.created";
	}

	@Override
	public HashMap<String, Serializable> toPrimitives() {
		HashMap<String, Serializable> primitives = new HashMap<>();
		primitives.put("name", name);
		primitives.put("ownerId", ownerId);
		primitives.put("createAt", createAt);
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
		return new TenantCreatedDomainEvent(
			aggregateId,
			(String) body.get("name"),
			(String) body.get("ownerId"),
			(String) body.get("createAt"),
			(String) body.get("updateAt")
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		TenantCreatedDomainEvent that = (TenantCreatedDomainEvent) o;
		return Objects.equals(name, that.name) && Objects.equals(ownerId, that.ownerId);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(ownerId);
		return result;
	}
}
