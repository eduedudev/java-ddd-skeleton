package com.jaapec.tenant.tenant.application.update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantIdMother;
import com.jaapec.tenant.tenant.domain.TenantMother;
import com.jaapec.tenant.tenant.domain.events.TenantUpdatedDomainEvent;

final class UpdateTenantCommandHandlerShould extends TenantModuleUnitTestCase {

	private UpdateTenantCommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new UpdateTenantCommandHandler(new TenantUpdater(repository, eventBus));
	}

	@Test
	void should_update_tenant_when_command_is_valid() {
		Tenant tenant = TenantMother.random();
		UpdateTenantCommand command = UpdateTenantCommandMother.random(tenant.id());

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenant));

		handler.handle(command);

		ArgumentCaptor<Tenant> savedTenantCaptor = ArgumentCaptor.forClass(Tenant.class);
		verify(repository).update(savedTenantCaptor.capture());

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<DomainEvent>> captor = ArgumentCaptor.forClass(List.class);
		verify(eventBus).publish(captor.capture());
		List<DomainEvent> capturedEvents = captor.getValue();
		assertEquals(1, capturedEvents.size());
		DomainEvent capturedEvent = capturedEvents.getFirst();
		assertInstanceOf(TenantUpdatedDomainEvent.class, capturedEvent);
		TenantUpdatedDomainEvent tenantUpdatedEvent = (TenantUpdatedDomainEvent) capturedEvent;
		assertEquals(tenant.id().value(), tenantUpdatedEvent.aggregateId());
		Map<String, Serializable> body = capturedEvent.toPrimitives();
		assertEquals(command.name(), body.get("name"));

		Tenant capturedTenant = savedTenantCaptor.getValue();
		assertEquals(command.id(), capturedTenant.id().value());
		assertEquals(command.name(), capturedTenant.name().value());
	}

	@Test
	void should_throw_exception_when_tenant_not_found() {
		UpdateTenantCommand command = UpdateTenantCommandMother.random(TenantIdMother.random());
		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}
}
