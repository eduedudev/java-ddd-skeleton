package com.jaapec.tenant.shared.infrastructure.bus.event.rabbitmq;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.infrastructure.bus.event.DomainEventSubscriber;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;

@Service
@DomainEventSubscriber({ TenantCreatedDomainEvent.class })
public final class TestAllWorksOnRabbitMqEventsPublished {

	public Boolean hasBeenExecuted = false;

	public void on(TenantCreatedDomainEvent event) {
		hasBeenExecuted = true;
	}
}
