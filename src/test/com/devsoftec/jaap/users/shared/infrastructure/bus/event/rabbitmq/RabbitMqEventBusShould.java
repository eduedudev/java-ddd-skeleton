package com.devsoftec.jaap.users.shared.infrastructure.bus.event.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.devsoftec.jaap.users.shared.infrastructure.InfrastructureTestCase;
import com.devsoftec.jaap.users.shared.infrastructure.bus.event.DomainEventSubscriberInformation;
import com.devsoftec.jaap.users.shared.infrastructure.bus.event.DomainEventSubscribersInformation;
import com.devsoftec.jaap.users.users.domain.UserCreatedDomainEventMother;
import com.devsoftec.jaap.users.users.domain.events.UserCreatedDomainEvent;

public final class RabbitMqEventBusShould extends InfrastructureTestCase {

	@Autowired
	private RabbitMqEventBus eventBus;

	@Autowired
	private RabbitMqDomainEventsConsumer consumer;

	@Autowired
	private TestAllWorksOnRabbitMqEventsPublished subscriber;

	@BeforeEach
	protected void setUp() {
		subscriber.hasBeenExecuted = false;

		consumer.withSubscribersInformation(
			new DomainEventSubscribersInformation(
				new HashMap<Class<?>, DomainEventSubscriberInformation>() {
					{
						put(
							TestAllWorksOnRabbitMqEventsPublished.class,
							new DomainEventSubscriberInformation(
								TestAllWorksOnRabbitMqEventsPublished.class,
								Collections.singletonList(UserCreatedDomainEvent.class)
							)
						);
					}
				}
			)
		);
	}

	@Test
	void publish_and_consume_domain_events_from_rabbitmq() throws Exception {
		UserCreatedDomainEvent domainEvent = UserCreatedDomainEventMother.random();

		eventBus.publish(Collections.singletonList(domainEvent));

		consumer.consume();

		eventually(() -> assertTrue(subscriber.hasBeenExecuted));
	}
}
