package com.devsoftec.jaap.users.shared.infrastructure.bus.event;

import java.lang.annotation.*;

import com.devsoftec.jaap.users.shared.domain.bus.event.DomainEvent;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DomainEventSubscriber {
	Class<? extends DomainEvent>[] value();
}
