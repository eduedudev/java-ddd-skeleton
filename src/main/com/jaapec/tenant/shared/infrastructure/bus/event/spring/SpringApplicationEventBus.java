package com.jaapec.tenant.shared.infrastructure.bus.event.spring;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Service
public class SpringApplicationEventBus implements EventBus {

	private final ApplicationEventPublisher publisher;

	public SpringApplicationEventBus(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public void publish(final List<DomainEvent> events) {
		events.forEach(this::publish);
	}

	private void publish(final DomainEvent event) {
		this.publisher.publishEvent(event);
	}
}
