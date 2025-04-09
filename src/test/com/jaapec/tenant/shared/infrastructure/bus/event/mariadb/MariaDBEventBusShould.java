package com.jaapec.tenant.shared.infrastructure.bus.event.mariadb;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.infrastructure.InfrastructureTestCase;
import com.jaapec.tenant.users.domain.UserCreatedDomainEventMother;
import com.jaapec.tenant.users.domain.events.UserCreatedDomainEvent;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class MariaDBEventBusShould extends InfrastructureTestCase {

	@Autowired
	private MariaDBEventBus eventBus;

	@Autowired
	private MariaDBDomainEventsConsumer consumer;

	@Autowired
	private SessionFactory sessionFactory;

	@Test
	void publish_and_consume_domain_events_from_mariadb() throws Exception {
		UserCreatedDomainEvent domainEvent = UserCreatedDomainEventMother.random();

		eventBus.publish(Collections.singletonList(domainEvent));

		Thread consumerProcess = new Thread(() -> consumer.consume());
		consumerProcess.start();

		eventually(() -> {
			List<?> remaining = sessionFactory
				.getCurrentSession()
				.createNativeQuery("SELECT * FROM domain_events WHERE id = :id",Void.class)
				.setParameter("id", domainEvent.eventId())
				.getResultList();

			assertTrue(remaining.isEmpty());
		});
	}


}
