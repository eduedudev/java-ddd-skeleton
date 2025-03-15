package com.devsoftec.jaap.users.users.infrastructure;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import com.devsoftec.jaap.users.shared.domain.UuidGenerator;
import com.devsoftec.jaap.users.shared.domain.bus.event.DomainEvent;
import com.devsoftec.jaap.users.shared.domain.bus.event.EventBus;
import com.devsoftec.jaap.users.shared.domain.query.Query;
import com.devsoftec.jaap.users.shared.domain.query.QueryBus;
import com.devsoftec.jaap.users.shared.domain.query.QueryNotRegisteredError;
import com.devsoftec.jaap.users.shared.domain.query.Response;

public abstract class UnitTestCase {

	protected EventBus eventBus;
	protected QueryBus queryBus;
	protected UuidGenerator uuidGenerator;

	@BeforeEach
	protected void setUp() {
		eventBus = mock(EventBus.class);
		queryBus = mock(QueryBus.class);
		uuidGenerator = mock(UuidGenerator.class);
	}

	public void shouldHavePublished(List<DomainEvent> domainEvents) {
		verify(eventBus, atLeastOnce()).publish(domainEvents);
	}

	public void shouldHavePublished(DomainEvent domainEvent) {
		shouldHavePublished(Collections.singletonList(domainEvent));
	}

	public void shouldGenerateUuid(String uuid) {
		when(uuidGenerator.generate()).thenReturn(uuid);
	}

	public void shouldGenerateUuids(String uuid, String... others) {
		when(uuidGenerator.generate()).thenReturn(uuid, others);
	}

	public void shouldAsk(Query query, Response response) throws QueryNotRegisteredError {
		when(queryBus.ask(query)).thenReturn(response);
	}
}
