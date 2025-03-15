package com.devsoftec.jaap.users.shared.infrastructure.bus.event.mariadb;

import java.util.Collections;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.devsoftec.jaap.users.shared.infrastructure.InfrastructureTestCase;
import com.devsoftec.jaap.users.users.domain.UserCreatedDomainEventMother;
import com.devsoftec.jaap.users.users.domain.events.UserCreatedDomainEvent;

@Transactional
class MariaDBEventBusShould extends InfrastructureTestCase {

	@Autowired
	private MariaDBEventBus eventBus;

	@Autowired
	private MariaDBDomainEventsConsumer consumer;

	@Test
	void publish_and_consume_domain_events_from_msql() throws InterruptedException {
		UserCreatedDomainEvent domainEvent = UserCreatedDomainEventMother.random();

		eventBus.publish(Collections.singletonList(domainEvent));

		Thread consumerProcess = new Thread(() -> consumer.consume());
		consumerProcess.start();

		Thread.sleep(100);
	}
}
