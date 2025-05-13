package com.jaapec.tenant.tenant.application.change_domain;

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

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.domain.*;
import com.jaapec.tenant.tenant.domain.events.TenantDomainChangedEvent;

final class ChangeTenantDomainCommandHandlerShould extends TenantModuleUnitTestCase {

	private ChangeTenantDomainCommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new ChangeTenantDomainCommandHandler(new TenantDomainChanger(repository, eventBus));
	}

	@Test
	void should_change_tenant_domain_when_command_is_valid() {
		Tenant tenant = TenantMother.random();
		ChangeTenantDomainCommand command = ChangeTenantDomainCommandMother.create(
			tenant.id(),
			TenantDomainMother.create("tenant.example.com")
		);

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
		assertInstanceOf(TenantDomainChangedEvent.class, capturedEvent);
		TenantDomainChangedEvent changeTenantDomainEvent = (TenantDomainChangedEvent) capturedEvent;
		assertEquals(tenant.id().value(), changeTenantDomainEvent.aggregateId());
		Map<String, Serializable> body = capturedEvent.toPrimitives();
		assertEquals(command.domain(), body.get("domain"));

		Tenant capturedTenant = savedTenantCaptor.getValue();
		assertEquals(command.id(), capturedTenant.id().value());
		assertEquals(command.domain(), capturedTenant.domain().value());
	}

	@Test
	void should_throw_exception_when_domain_invalid() {
		assertThrows(InvalidDomainException.class, () -> TenantDomainMother.create("domain-custom"));
	}

	@Test
	void should_not_throw_exception_when_domain_is_valid() {
		assertDoesNotThrow(() ->
			ChangeTenantDomainCommandMother.create(TenantIdMother.random(), TenantDomainMother.create("tenant.example.com"))
		);
	}
}
