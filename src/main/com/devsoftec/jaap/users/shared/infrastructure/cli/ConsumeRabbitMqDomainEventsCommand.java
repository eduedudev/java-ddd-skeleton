package com.devsoftec.jaap.users.shared.infrastructure.cli;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.infrastructure.bus.event.rabbitmq.RabbitMqDomainEventsConsumer;

@Service
public final class ConsumeRabbitMqDomainEventsCommand extends ConsoleCommand {
    private final RabbitMqDomainEventsConsumer consumer;

    public ConsumeRabbitMqDomainEventsCommand(RabbitMqDomainEventsConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void execute(String[] args) {
        consumer.consume();
    }
}

