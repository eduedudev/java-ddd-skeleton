package com.jaapec.tenant.shared.infrastructure.bus.event;

import java.util.*;

import org.reflections.Reflections;

import com.jaapec.tenant.shared.domain.Service;

@Service
public final class DomainEventSubscribersInformation {
	Map<Class<?>, DomainEventSubscriberInformation> information;

	public DomainEventSubscribersInformation(Map<Class<?>, DomainEventSubscriberInformation> information) {
		this.information = information;
	}

	public DomainEventSubscribersInformation() {
		this(scanDomainEventSubscribers());
	}

	private static Map<Class<?>, DomainEventSubscriberInformation> scanDomainEventSubscribers() {
		Reflections   reflections = new Reflections("com.jaapec");
		Set<Class<?>> subscribers = reflections.getTypesAnnotatedWith(DomainEventSubscriber.class);

		Map<Class<?>, DomainEventSubscriberInformation> subscribersInformation = new HashMap<>();

		for (Class<?> subscriberClass : subscribers) {
			DomainEventSubscriber annotation = subscriberClass.getAnnotation(DomainEventSubscriber.class);

			subscribersInformation.put(
				subscriberClass,
				new DomainEventSubscriberInformation(subscriberClass, Arrays.asList(annotation.value()))
			);
		}

		return subscribersInformation;
	}

	public Collection<DomainEventSubscriberInformation> all() {
		return information.values();
	}

	public String[] rabbitMqFormattedNames() {
		return information.values()
			.stream()
			.map(DomainEventSubscriberInformation::formatRabbitMqQueueName)
			.distinct()
			.toArray(String[]::new);
	}
}
