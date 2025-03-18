package com.jaapec.tenant.shared.infrastructure.bus.event.mariadb;

import org.springframework.scheduling.annotation.Scheduled;

import com.jaapec.tenant.shared.domain.Service;

@Service
public final class MariaDBDomainEventsScheduler {

	private final MariaDBDomainEventsConsumer consumer;

	public MariaDBDomainEventsScheduler(MariaDBDomainEventsConsumer consumer) {
		this.consumer = consumer;
	}

	@Scheduled(fixedDelay = 60000)
	public void scheduleEventConsumption() {
		try {
			consumer.consume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
