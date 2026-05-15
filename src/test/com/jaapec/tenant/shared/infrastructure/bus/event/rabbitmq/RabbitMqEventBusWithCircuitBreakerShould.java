package com.jaapec.tenant.shared.infrastructure.bus.event.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;

import com.jaapec.tenant.shared.domain.circuit_breaker.CircuitBreaker;
import com.jaapec.tenant.shared.domain.circuit_breaker.CircuitBreakerState;
import com.jaapec.tenant.shared.infrastructure.bus.event.mariadb.MariaDBEventBus;
import com.jaapec.tenant.shared.infrastructure.circuit_breaker.SimpleCircuitBreaker;
import com.jaapec.tenant.tenant.domain.TenantCreatedDomainEventMother;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;

class RabbitMqEventBusWithCircuitBreakerShould {

	private RabbitMqPublisher publisher;
	private MariaDBEventBus fallback;
	private CircuitBreaker circuitBreaker;
	private RabbitMqEventBus eventBus;

	@BeforeEach
	void setUp() {
		publisher = mock(RabbitMqPublisher.class);
		fallback = mock(MariaDBEventBus.class);
		circuitBreaker = new SimpleCircuitBreaker(2, 30_000, 1);
		eventBus = new RabbitMqEventBus(publisher, fallback, circuitBreaker);
	}

	@Test
	void publish_event_to_rabbitmq_when_circuit_is_closed() {
		TenantCreatedDomainEvent event = TenantCreatedDomainEventMother.random();

		eventBus.publish(Collections.singletonList(event));

		verify(publisher).publish(event, "domain_events");
		verifyNoInteractions(fallback);
	}

	@Test
	void fallback_to_mariadb_when_rabbitmq_fails() {
		TenantCreatedDomainEvent event = TenantCreatedDomainEventMother.random();
		doThrow(new AmqpException("connection refused")).when(publisher).publish(event, "domain_events");

		eventBus.publish(Collections.singletonList(event));

		verify(fallback).publish(Collections.singletonList(event));
		assertEquals(CircuitBreakerState.CLOSED, circuitBreaker.state());
	}

	@Test
	void fallback_directly_when_circuit_is_open() {
		TenantCreatedDomainEvent event1 = TenantCreatedDomainEventMother.random();
		TenantCreatedDomainEvent event2 = TenantCreatedDomainEventMother.random();
		TenantCreatedDomainEvent event3 = TenantCreatedDomainEventMother.random();

		doThrow(new AmqpException("connection refused")).when(publisher).publish(any(), anyString());

		eventBus.publish(Collections.singletonList(event1));
		eventBus.publish(Collections.singletonList(event2));
		assertEquals(CircuitBreakerState.OPEN, circuitBreaker.state());

		eventBus.publish(Collections.singletonList(event3));

		verify(publisher, times(2)).publish(any(), anyString());
		verify(fallback, times(3)).publish(anyList());
	}

	@Test
	void recover_after_circuit_half_open_success() throws Exception {
		TenantCreatedDomainEvent event1 = TenantCreatedDomainEventMother.random();
		TenantCreatedDomainEvent event2 = TenantCreatedDomainEventMother.random();

		doThrow(new AmqpException("connection refused")).when(publisher).publish(any(), anyString());

		eventBus.publish(Collections.singletonList(event1));
		eventBus.publish(Collections.singletonList(event2));
		assertEquals(CircuitBreakerState.OPEN, circuitBreaker.state());

		reset(publisher);
		TenantCreatedDomainEvent recoveryEvent = TenantCreatedDomainEventMother.random();

		circuitBreaker.reset();

		eventBus.publish(Collections.singletonList(recoveryEvent));

		verify(publisher).publish(recoveryEvent, "domain_events");
	}
}
