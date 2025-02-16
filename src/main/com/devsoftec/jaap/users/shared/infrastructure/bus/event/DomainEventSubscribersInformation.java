package com.devsoftec.jaap.users.shared.infrastructure.bus.event;


import com.devsoftec.jaap.users.shared.domain.Service;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

@Service
public final class DomainEventSubscribersInformation {
    HashMap<Class<?>, DomainEventSubscriberInformation> information;

    public DomainEventSubscribersInformation(HashMap<Class<?>, DomainEventSubscriberInformation> hashMap) {
        Reflections reflections = new Reflections("com.devsoftec");
        Set<Class<?>> classes     = reflections.getTypesAnnotatedWith(DomainEventSubscriber.class);

        information = formatSubscribers(classes);
    }

    public Collection<DomainEventSubscriberInformation> all() {
        return information.values();
    }

    public String[] rabbitMqFormattedNames() {
        return information.values()
                .stream()
                .map(DomainEventSubscriberInformation::formatRabbitMqQueueName)
                .distinct()
                .toArray(String[]::new);
    }

    private HashMap<Class<?>, DomainEventSubscriberInformation> formatSubscribers(Set<Class<?>> subscribers) {
        HashMap<Class<?>, DomainEventSubscriberInformation> subscribersInformation = new HashMap<>();

        for (Class<?> subscriberClass : subscribers) {
            DomainEventSubscriber annotation = subscriberClass.getAnnotation(DomainEventSubscriber.class);

            subscribersInformation.put(
                    subscriberClass,
                    new DomainEventSubscriberInformation(subscriberClass, Arrays.asList(annotation.value()))
            );
        }

        return subscribersInformation;
    }
}
