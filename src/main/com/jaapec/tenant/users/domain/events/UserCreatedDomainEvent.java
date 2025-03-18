package com.jaapec.tenant.users.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class UserCreatedDomainEvent extends DomainEvent {

	private final String name;
	private final String email;

	public UserCreatedDomainEvent() {
		super(null);
		this.name = "";
		this.email = "";
	}

	public UserCreatedDomainEvent(String aggregateId, String name, String email) {
		super(aggregateId);
		this.name = name;
		this.email = email;
	}

	public UserCreatedDomainEvent(String aggregateId, String eventId, String occurredOn, String name, String email) {
		super(aggregateId, eventId, occurredOn);
		this.name = name;
		this.email = email;
	}

	public UserCreatedDomainEvent(String name, String email) {
		this.name = name;
		this.email = email;
	}

	@Override
	public String eventName() {
		return "user.created";
	}

	@Override
	public HashMap<String, Serializable> toPrimitives() {
		return new HashMap<String, Serializable>() {
			{
				put("name", name);
				put("email", email);
			}
		};
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		HashMap<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new UserCreatedDomainEvent(
			aggregateId,
			eventId,
			occurredOn,
			(String) body.get("name"),
			(String) body.get("email")
		);
	}

	public String name() {
		return name;
	}

	public String email() {
		return email;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		UserCreatedDomainEvent that = (UserCreatedDomainEvent) o;
		return Objects.equals(name, that.name) && Objects.equals(email, that.email);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(email);
		return result;
	}
}
