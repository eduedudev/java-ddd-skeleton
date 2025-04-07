package com.jaapec.tenant.shared.infrastructure.bus.event;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.Utils;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

@Service
public final class DomainEventJsonDeserializer {

	private final DomainEventsInformation information;

	public DomainEventJsonDeserializer(DomainEventsInformation information) {
		this.information = information;
	}

	public DomainEvent deserialize(String body) throws ReflectiveOperationException {
		Map<String, Serializable> eventData = Utils.jsonDecode(body);
		if (eventData == null) {
			throw new IllegalArgumentException("Invalid JSON: eventData is null");
		}

		Map<String, Serializable> data = getMap(eventData, "data");
		Map<String, Serializable> attributes = getMap(data, "attributes");
		String type = (String) data.get("type");

		if (type == null) {
			throw new IllegalArgumentException("Missing event type in data");
		}

		Class<?> domainEventClass = information.forName(type);
		DomainEvent nullInstance = (DomainEvent) domainEventClass.getConstructor().newInstance();

		Method fromPrimitivesMethod = domainEventClass.getMethod(
			"fromPrimitives",
			String.class,
			Map.class,
			String.class,
			String.class
		);

		Object domainEvent = fromPrimitivesMethod.invoke(
			nullInstance,
			attributes.get("id").toString(),
			attributes,
			data.get("id").toString(),
			data.get("occurred_on").toString()
		);
		return (DomainEvent) domainEvent;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Serializable> getMap(Map<String, Serializable> source, String key) {
		return Optional
			.ofNullable((HashMap<String, Serializable>) source.get(key))
			.orElseThrow(() -> new IllegalArgumentException("Missing key: " + key));
	}
}
