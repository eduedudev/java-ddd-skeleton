package com.jaapec.tenant.shared.infrastructure.bus.event.mariadb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.Utils;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.infrastructure.bus.event.DomainEventsInformation;
import com.jaapec.tenant.shared.infrastructure.bus.event.spring.SpringApplicationEventBus;

@Service
public class MariaDBDomainEventsConsumer {

	private final SessionFactory sessionFactory;
	private final DomainEventsInformation domainEventsInformation;
	private final SpringApplicationEventBus bus;

	public MariaDBDomainEventsConsumer(
		SessionFactory sessionFactory,
		DomainEventsInformation domainEventsInformation,
		SpringApplicationEventBus bus
	) {
		this.sessionFactory = sessionFactory;
		this.domainEventsInformation = domainEventsInformation;
		this.bus = bus;
	}

	@Transactional
	public void consume() {
		try (Session session = sessionFactory.openSession()) {
			String sqlQuery =
				"SELECT id, aggregate_id, name, body, occurred_on FROM domain_events ORDER BY occurred_on LIMIT :chunk";
			NativeQuery<Object[]> query = session.createNativeQuery(sqlQuery, Object[].class);

			final Integer CHUNKS = 10;
			query.setParameter("chunk", CHUNKS);

			List<Object[]> events = query.getResultList();

			for (Object[] event : events) {
				try {
					executeSubscribers(
						(String) event[0], // id
						(String) event[1], // aggregateId
						(String) event[2], // eventName
						(String) event[3], // body
						(Timestamp) event[4] // occurredOn
					);
					deleteEvent((String) event[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void deleteEvent(String id) {
		try (Session session = sessionFactory.openSession()) {
			NativeQuery<Void> query = session.createNativeQuery("DELETE FROM domain_events WHERE id = :id", Void.class);
			query.setParameter("id", id);
			query.executeUpdate();
		}
	}

	private void executeSubscribers(String id, String aggregateId, String eventName, String body, Timestamp occurredOn)
		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<? extends DomainEvent> domainEventClass = domainEventsInformation.forName(eventName);

		DomainEvent nullInstance = domainEventClass.getConstructor().newInstance();

		Method fromPrimitivesMethod = domainEventClass.getMethod(
			"fromPrimitives",
			String.class,
			HashMap.class,
			String.class,
			String.class
		);

		Object domainEvent = fromPrimitivesMethod.invoke(
			nullInstance,
			aggregateId,
			Utils.jsonDecode(body),
			id,
			Utils.dateToString(occurredOn)
		);

		bus.publish(Collections.singletonList((DomainEvent) domainEvent));
	}
}
