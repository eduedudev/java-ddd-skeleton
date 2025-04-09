package com.jaapec.tenant.shared.infrastructure.bus.event.mariadb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.Utils;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Transactional
@Service
public class MariaDBEventBus implements EventBus {

	private final SessionFactory sessionFactory;

	public MariaDBEventBus(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void publish(List<DomainEvent> events) {
		events.forEach(this::publish);
	}

	private void publish(DomainEvent domainEvent) {
		String id = domainEvent.eventId();
		String aggregateId = domainEvent.aggregateId();
		String name = domainEvent.eventName();
		Map<String, Serializable> body = domainEvent.toPrimitives();
		String occurredOn = domainEvent.occurredOn();

		String queryString =
			"INSERT INTO domain_events (id, aggregate_id, name, body, occurred_on) " +
			"VALUES (:id, :aggregateId, :name, :body, :occurredOn)";

		try {
			NativeQuery<Void> query = sessionFactory.getCurrentSession().createNativeQuery(queryString, Void.class);
			query.setParameter("id", id);
			query.setParameter("aggregateId", aggregateId);
			query.setParameter("name", name);
			query.setParameter("body", Utils.jsonEncode(body));
			query.setParameter("occurredOn", occurredOn);
			query.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Error publishing the event to MariaDB", e);
		}
	}
}
