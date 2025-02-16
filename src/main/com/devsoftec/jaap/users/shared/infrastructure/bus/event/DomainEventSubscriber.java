package com.devsoftec.jaap.users.shared.infrastructure.bus.event;

import com.devsoftec.jaap.users.shared.domain.bus.event.DomainEvent;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DomainEventSubscriber {
    Class<? extends DomainEvent>[] value();
}
