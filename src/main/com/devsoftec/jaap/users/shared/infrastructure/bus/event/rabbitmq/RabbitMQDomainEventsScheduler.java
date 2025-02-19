package com.devsoftec.jaap.users.shared.infrastructure.bus.event.rabbitmq;

import com.devsoftec.jaap.users.shared.domain.Service;
import org.springframework.scheduling.annotation.Scheduled;

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
