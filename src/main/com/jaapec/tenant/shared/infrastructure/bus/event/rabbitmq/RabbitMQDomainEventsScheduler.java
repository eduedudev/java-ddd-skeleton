package com.jaapec.tenant.shared.infrastructure.bus.event.rabbitmq;

import org.springframework.scheduling.annotation.Scheduled;

import com.jaapec.tenant.shared.domain.Service;

@Service
public final class RabbitMQDomainEventsScheduler {

	private final RabbitMqDomainEventsConsumer consumer;

	public RabbitMQDomainEventsScheduler(RabbitMqDomainEventsConsumer consumer) {
		this.consumer = consumer;
	}

	@Scheduled(fixedDelay = 5000)
	public void scheduleEventConsumption() {
		try {
			consumer.consume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
