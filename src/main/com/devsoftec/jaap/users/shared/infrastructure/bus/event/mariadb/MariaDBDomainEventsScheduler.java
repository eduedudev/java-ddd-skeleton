package com.devsoftec.jaap.users.shared.infrastructure.bus.event.mariadb;

import com.devsoftec.jaap.users.shared.domain.Service;
import org.springframework.scheduling.annotation.Scheduled;

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
