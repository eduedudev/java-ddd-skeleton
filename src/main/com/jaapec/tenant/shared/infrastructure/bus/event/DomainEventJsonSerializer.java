package com.jaapec.tenant.shared.infrastructure.bus.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jaapec.tenant.shared.domain.Utils;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class DomainEventJsonSerializer {

	public static String serialize(DomainEvent domainEvent) {
		Map<String, Serializable> attributes = domainEvent.toPrimitives();
		attributes.put("id", domainEvent.aggregateId());

		Map<String, Serializable> data = new HashMap<String, Serializable>();
		data.put("id", domainEvent.eventId());
		data.put("type", domainEvent.eventName());
		data.put("occurred_on", domainEvent.occurredOn());
		data.put("attributes", (Serializable) attributes);

		HashMap<String, Serializable> json = new HashMap<String, Serializable>();
		json.put("data", (Serializable) data);
		json.put("meta", new HashMap<String, Serializable>());
		return Utils.jsonEncode(json);
	}
}
