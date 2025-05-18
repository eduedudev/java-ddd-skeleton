package com.jaapec.tenant.shared.infrastructure.bus.event.mariadb;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.infrastructure.InfrastructureTestCase;
import com.jaapec.tenant.tenant.domain.TenantCreatedDomainEventMother;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;

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
		TenantCreatedDomainEvent domainEvent = TenantCreatedDomainEventMother.random();

		eventBus.publish(Collections.singletonList(domainEvent));

		Thread consumerProcess = new Thread(() -> consumer.consume());
		consumerProcess.start();

		eventually(() -> {
			List<?> remaining = sessionFactory
				.getCurrentSession()
				.createNativeQuery("SELECT * FROM domain_events WHERE id = :id", Void.class)
				.setParameter("id", domainEvent.eventId())
				.getResultList();

			assertTrue(remaining.isEmpty());
		});
	}

	@Test
	void throw_persistence_exception_when_publishing_event_fails() {
		TenantCreatedDomainEvent invalidEvent = new TenantCreatedDomainEvent(null, null, null, null, null);
		List<DomainEvent> events = List.of(invalidEvent);
		PersistenceException exception = assertThrows(PersistenceException.class, () -> eventBus.publish(events));

		assertTrue(exception.getMessage().contains("Error publishing the event to MariaDB"));
	}
}
