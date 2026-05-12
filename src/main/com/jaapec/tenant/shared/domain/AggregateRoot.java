package com.jaapec.tenant.shared.domain;

import java.util.ArrayList;
import java.util.List;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public abstract class AggregateRoot {

	private List<DomainEvent> domainEvents = new ArrayList<>();

	public final List<DomainEvent> pullDomainEvents() {
		List<DomainEvent> events = List.copyOf(domainEvents);
		domainEvents = new ArrayList<>();
		return events;
	}

	protected final void record(DomainEvent event) {
		domainEvents.add(event);
	}
}
