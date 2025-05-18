package com.jaapec.tenant.shared.infrastructure.bus.event.rabbitmq;

import java.util.Collections;
import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.context.annotation.Primary;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.shared.infrastructure.bus.event.mariadb.MariaDBEventBus;

@Service
@Primary
public final class RabbitMqEventBus implements EventBus {

	private final RabbitMqPublisher publisher;
	private final MariaDBEventBus failoverPublisher;
	private final String exchangeName;

	public RabbitMqEventBus(RabbitMqPublisher publisher, MariaDBEventBus failoverPublisher) {
		this.publisher = publisher;
		this.failoverPublisher = failoverPublisher;
		this.exchangeName = "domain_events";
	}

	@Override
	public void publish(List<DomainEvent> events) {
		events.forEach(this::publish);
	}

	private void publish(DomainEvent domainEvent) {
		try {
			this.publisher.publish(domainEvent, exchangeName);
		} catch (AmqpException error) {
			failoverPublisher.publish(Collections.singletonList(domainEvent));
		}
	}
}
