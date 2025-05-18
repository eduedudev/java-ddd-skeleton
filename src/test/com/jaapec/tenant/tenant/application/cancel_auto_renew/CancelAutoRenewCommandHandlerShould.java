package com.jaapec.tenant.tenant.application.cancel_auto_renew;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.subscription.domain.SubscriptionIsInactiveException;
import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;
import com.jaapec.tenant.tenant.domain.events.TenantSubscriptionAutoRenewCanceledEvent;

final class CancelAutoRenewCommandHandlerShould extends TenantModuleUnitTestCase {

	private CancelAutoRenewCommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
		handler = new CancelAutoRenewCommandHandler(new SubscriptionAutoRenewCanceler(repository, eventBus));
	}

	@Test
	void should_cancel_auto_renew_when_command_is_valid() {
		// Arrange
		Tenant tenant = TenantMother.random();
		String subscriptionId = UUID.randomUUID().toString();

		// Create a tenant with a subscription that can be modified
		Tenant tenantWithSubscription = mock(Tenant.class);
		when(tenantWithSubscription.id()).thenReturn(tenant.id());

		// Mock the cancelAutoRenew method to return a new tenant
		Tenant tenantAfterCancellation = mock(Tenant.class);
		when(tenantWithSubscription.cancelAutoRenew(any())).thenReturn(tenantAfterCancellation);

		CancelAutoRenewCommand command = CancelAutoRenewCommandMother.create(tenant.id().value(), subscriptionId);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithSubscription));

		// Act
		handler.handle(command);

		// Assert
		verify(repository).update(tenantAfterCancellation);
		verify(eventBus).publish(any());
	}

	@Test
	void should_publish_event_when_auto_renew_is_canceled() {
		// Arrange
		Tenant tenant = TenantMother.random();
		String subscriptionId = UUID.randomUUID().toString();

		// Create a tenant with a subscription that can be modified
		Tenant tenantWithSubscription = mock(Tenant.class);
		when(tenantWithSubscription.id()).thenReturn(tenant.id());

		// Mock the cancelAutoRenew method to return a new tenant with events
		Tenant tenantAfterCancellation = mock(Tenant.class);
		when(tenantWithSubscription.cancelAutoRenew(any())).thenReturn(tenantAfterCancellation);

		// Create a domain event
		TenantSubscriptionAutoRenewCanceledEvent event = new TenantSubscriptionAutoRenewCanceledEvent(
			tenant.id().value(),
			subscriptionId,
			"plan-id",
			"Plan Name",
			"Plan Description"
		);

		List<DomainEvent> events = List.of(event);
		when(tenantAfterCancellation.pullDomainEvents()).thenReturn(events);

		CancelAutoRenewCommand command = CancelAutoRenewCommandMother.create(tenant.id().value(), subscriptionId);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithSubscription));

		// Act
		handler.handle(command);

		// Assert
		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<DomainEvent>> captor = ArgumentCaptor.forClass(List.class);
		verify(eventBus).publish(captor.capture());
		List<DomainEvent> capturedEvents = captor.getValue();
		assertEquals(1, capturedEvents.size());
		DomainEvent capturedEvent = capturedEvents.getFirst();
		assertInstanceOf(TenantSubscriptionAutoRenewCanceledEvent.class, capturedEvent);
		TenantSubscriptionAutoRenewCanceledEvent cancelEvent = (TenantSubscriptionAutoRenewCanceledEvent) capturedEvent;
		assertEquals(tenant.id().value(), cancelEvent.aggregateId());
		Map<String, Serializable> body = capturedEvent.toPrimitives();
		assertEquals(subscriptionId, body.get("subscriptionId"));
	}

	@Test
	void should_throw_exception_when_tenant_not_found() {
		// Arrange
		CancelAutoRenewCommand command = CancelAutoRenewCommandMother.random();
		when(repository.find(any())).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}

	@Test
	void should_throw_exception_when_subscription_is_inactive() {
		// Arrange
		Tenant tenant = TenantMother.random();
		String subscriptionId = UUID.randomUUID().toString();

		// Create a tenant with an inactive subscription
		Tenant tenantWithInactiveSubscription = mock(Tenant.class);
		when(tenantWithInactiveSubscription.id()).thenReturn(tenant.id());

		// Mock the cancelAutoRenew method to throw SubscriptionIsInactiveException
		when(tenantWithInactiveSubscription.cancelAutoRenew(any())).thenThrow(new SubscriptionIsInactiveException());

		CancelAutoRenewCommand command = CancelAutoRenewCommandMother.create(tenant.id().value(), subscriptionId);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithInactiveSubscription));

		// Act & Assert
		assertThrows(SubscriptionIsInactiveException.class, () -> handler.handle(command));
	}
}
