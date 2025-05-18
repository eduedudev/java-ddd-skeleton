package com.jaapec.tenant.shared.infrastructure.bus.event.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.infrastructure.InfrastructureTestCase;
import com.jaapec.tenant.shared.infrastructure.bus.event.DomainEventSubscriberInformation;
import com.jaapec.tenant.shared.infrastructure.bus.event.DomainEventSubscribersInformation;
import com.jaapec.tenant.tenant.domain.TenantCreatedDomainEventMother;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;

final class RabbitMqEventBusShould extends InfrastructureTestCase {

	@Autowired
	private RabbitMqEventBus eventBus;

	@Autowired
	private RabbitMqDomainEventsConsumer consumer;

	@Autowired
	private TestAllWorksOnRabbitMqEventsPublished subscriber;

	@BeforeEach
	void setUp() {
		subscriber.hasBeenExecuted = false;

		consumer.withSubscribersInformation(
			new DomainEventSubscribersInformation(
				new HashMap<Class<?>, DomainEventSubscriberInformation>() {
					{
						put(
							TestAllWorksOnRabbitMqEventsPublished.class,
							new DomainEventSubscriberInformation(
								TestAllWorksOnRabbitMqEventsPublished.class,
								Collections.singletonList(TenantCreatedDomainEvent.class)
							)
						);
					}
				}
			)
		);
	}

	@Test
	void publish_and_consume_domain_events_from_rabbitmq() throws Exception {
		TenantCreatedDomainEvent domainEvent = TenantCreatedDomainEventMother.random();

		eventBus.publish(Collections.singletonList(domainEvent));

		consumer.consume();

		eventually(() -> assertTrue(subscriber.hasBeenExecuted));
	}
}
