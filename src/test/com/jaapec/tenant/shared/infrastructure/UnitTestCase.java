package com.jaapec.tenant.shared.infrastructure;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import com.jaapec.tenant.shared.domain.UuidGenerator;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.shared.domain.bus.query.Query;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.domain.bus.query.Response;

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

	public void shouldAsk(Query query, Response response) {
		when(queryBus.ask(query)).thenReturn(response);
	}
}
