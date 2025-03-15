package com.devsoftec.jaap.users.shared.infrastructure.bus.event.rabbitmq;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.infrastructure.bus.event.DomainEventSubscriber;
import com.devsoftec.jaap.users.users.domain.events.UserCreatedDomainEvent;

@Service
@DomainEventSubscriber({ UserCreatedDomainEvent.class })
public final class TestAllWorksOnRabbitMqEventsPublished {

	public Boolean hasBeenExecuted = false;

	public void on(UserCreatedDomainEvent event) {
		hasBeenExecuted = true;
	}
}
