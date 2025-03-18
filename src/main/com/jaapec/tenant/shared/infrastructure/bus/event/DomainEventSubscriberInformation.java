package com.jaapec.tenant.shared.infrastructure.bus.event;

import java.util.List;

import com.jaapec.tenant.shared.domain.Utils;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class DomainEventSubscriberInformation {

	private final Class<?> subscriberClass;
	private final List<Class<? extends DomainEvent>> subscribedEvents;

	public DomainEventSubscriberInformation(
		Class<?> subscriberClass,
		List<Class<? extends DomainEvent>> subscribedEvents
	) {
		this.subscriberClass = subscriberClass;
		this.subscribedEvents = subscribedEvents;
	}

	public Class<?> subscriberClass() {
		return subscriberClass;
	}

	public String contextName() {
		String[] nameParts = subscriberClass.getName().split("\\.");

		return nameParts[2];
	}

	public String moduleName() {
		String[] nameParts = subscriberClass.getName().split("\\.");

		return nameParts[3];
	}

	public String className() {
		String[] nameParts = subscriberClass.getName().split("\\.");

		return nameParts[nameParts.length - 1];
	}

	public List<Class<? extends DomainEvent>> subscribedEvents() {
		return subscribedEvents;
	}

	public String formatRabbitMqQueueName() {
		return String.format("jaap.%s.%s.%s", contextName(), moduleName(), Utils.toSnake(className()));
	}
}
