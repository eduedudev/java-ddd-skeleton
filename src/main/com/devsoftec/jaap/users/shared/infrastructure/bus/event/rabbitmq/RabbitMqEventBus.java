package com.devsoftec.jaap.users.shared.infrastructure.bus.event.rabbitmq;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.bus.event.EventBus;
import com.devsoftec.jaap.users.shared.domain.bus.event.DomainEvent;
import com.devsoftec.jaap.users.shared.infrastructure.bus.event.mariadb.MariaDBEventBus;
import org.springframework.context.annotation.Primary;
import org.springframework.amqp.AmqpException;

import java.util.Collections;
import java.util.List;

@Service
@Primary
public class RabbitMqEventBus implements EventBus {
    private final RabbitMqPublisher publisher;
    private final MariaDBEventBus failoverPublisher;
    private final String            exchangeName;

    public RabbitMqEventBus(RabbitMqPublisher publisher, MariaDBEventBus failoverPublisher) {
        this.publisher         = publisher;
        this.failoverPublisher = failoverPublisher;
        this.exchangeName      = "domain_events";
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
