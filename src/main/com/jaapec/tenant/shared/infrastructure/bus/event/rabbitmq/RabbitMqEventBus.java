package com.jaapec.tenant.shared.infrastructure.bus.event.rabbitmq;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.shared.domain.circuit_breaker.CircuitBreaker;
import com.jaapec.tenant.shared.infrastructure.bus.event.mariadb.MariaDBEventBus;
import com.jaapec.tenant.shared.infrastructure.circuit_breaker.SimpleCircuitBreaker;

@Service
@Primary
public final class RabbitMqEventBus implements EventBus {

	private final RabbitMqPublisher publisher;
	private final MariaDBEventBus failoverPublisher;
	private final String exchangeName;
	private final CircuitBreaker circuitBreaker;

	public RabbitMqEventBus(RabbitMqPublisher publisher, MariaDBEventBus failoverPublisher) {
		this.publisher = publisher;
		this.failoverPublisher = failoverPublisher;
		this.exchangeName = "domain_events";
		this.circuitBreaker = new SimpleCircuitBreaker(3, 30_000, 1);
	}

	RabbitMqEventBus(RabbitMqPublisher publisher, MariaDBEventBus failoverPublisher, CircuitBreaker circuitBreaker) {
		this.publisher = publisher;
		this.failoverPublisher = failoverPublisher;
		this.exchangeName = "domain_events";
		this.circuitBreaker = circuitBreaker;
	}

	@Override
	public void publish(List<DomainEvent> events) {
		events.forEach(this::publish);
	}

	private void publish(DomainEvent domainEvent) {
		circuitBreaker.call(
			() -> publisher.publish(domainEvent, exchangeName),
			() -> failoverPublisher.publish(Collections.singletonList(domainEvent))
		);
	}
}
