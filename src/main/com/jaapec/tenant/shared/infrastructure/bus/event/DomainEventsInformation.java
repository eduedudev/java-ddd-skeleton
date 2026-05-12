package com.jaapec.tenant.shared.infrastructure.bus.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.reflections.Reflections;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

@Service
public final class DomainEventsInformation {

	Map<String, Class<? extends DomainEvent>> indexedDomainEvents;

	public DomainEventsInformation() {
		Reflections reflections = new Reflections("com.jaapec");
		Set<Class<? extends DomainEvent>> classes = reflections.getSubTypesOf(DomainEvent.class);

		try {
			indexedDomainEvents = formatEvents(classes);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public Class<? extends DomainEvent> forName(String name) {
		return indexedDomainEvents.get(name);
	}

	private Map<String, Class<? extends DomainEvent>> formatEvents(Set<Class<? extends DomainEvent>> domainEvents)
		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Map<String, Class<? extends DomainEvent>> events = new HashMap<>();

		for (Class<? extends DomainEvent> domainEvent : domainEvents) {
			if (Modifier.isAbstract(domainEvent.getModifiers())) continue;
			Constructor<? extends DomainEvent> constructor = domainEvent.getDeclaredConstructor();
			constructor.setAccessible(true);
			DomainEvent nullInstance = constructor.newInstance();

			events.put((String) domainEvent.getMethod("eventName").invoke(nullInstance), domainEvent);
		}

		return events;
	}

	public String forClass(Class<? extends DomainEvent> domainEventClass) {
		return indexedDomainEvents
			.entrySet()
			.stream()
			.filter(entry -> Objects.equals(entry.getValue(), domainEventClass))
			.map(Map.Entry::getKey)
			.findFirst()
			.orElse("");
	}
}
