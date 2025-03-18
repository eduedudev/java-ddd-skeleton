package com.jaapec.tenant.shared.infrastructure.bus.event;

import java.lang.annotation.*;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DomainEventSubscriber {
	Class<? extends DomainEvent>[] value();
}
