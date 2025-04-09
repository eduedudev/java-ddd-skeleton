package com.jaapec.tenant.shared.infrastructure.bus.event.rabbitmq;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.infrastructure.bus.event.DomainEventSubscriber;
import com.jaapec.tenant.users.domain.events.UserCreatedDomainEvent;

@Service
@DomainEventSubscriber({ UserCreatedDomainEvent.class })
public final class TestAllWorksOnRabbitMqEventsPublished {

	public Boolean hasBeenExecuted = false;

	public void on(UserCreatedDomainEvent event) {
		hasBeenExecuted = true;
	}
}
