package com.jaapec.tenant.shared.infrastructure.bus.event;

import java.util.List;

import com.jaapec.tenant.shared.domain.Utils;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public record DomainEventSubscriberInformation(
	Class<?> subscriberClass,
	List<Class<? extends DomainEvent>> subscribedEvents
) {
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

	public String formatRabbitMqQueueName() {
		return String.format("jaap.%s.%s.%s", contextName(), moduleName(), Utils.toSnake(className()));
	}
}
