package com.jaapec.tenant.tenant.application.add_subscription;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.domain.ActiveSubscriptionAlreadyExistsException;
import com.jaapec.tenant.tenant.domain.PendingSubscriptionExistsException;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;
import com.jaapec.tenant.tenant.domain.events.TenantSubscribeToPlanEvent;

final class AddSubscriptionCommandHandlerShould extends TenantModuleUnitTestCase {

	private AddSubscriptionCommandHandler handler;
	private PlanRepository planRepository;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
		planRepository = mock(PlanRepository.class);
		handler = new AddSubscriptionCommandHandler(new SubscriptionAdder(planRepository, repository, eventBus));
	}

	@Test
	void should_add_subscription_when_command_is_valid() {
		// Arrange
		Tenant tenant = TenantMother.random();
		Plan plan = PlanMother.random();
		String subscriptionId = java.util.UUID.randomUUID().toString();
		String billingIntervalValue = "MONTHLY";
		int pricingValue = 100;
		String currencyValue = "USD";
		String couponValue = "COUPON123";
		String sourceValue = "web";
		boolean autoRenewValue = true;

		AddSubscriptionCommand command = AddSubscriptionCommandMother.create(
			subscriptionId,
			tenant.id().value(),
			plan.id().value(),
			billingIntervalValue,
			pricingValue,
			currencyValue,
			couponValue,
			sourceValue,
			autoRenewValue
		);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenant));
		when(planRepository.find(plan.id())).thenReturn(Optional.of(plan));

		// Act
		handler.handle(command);

		// Assert
		ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
		verify(repository).update(tenantCaptor.capture());

		Tenant capturedTenant = tenantCaptor.getValue();
		assertEquals(tenant.id().value(), capturedTenant.id().value());

		verify(eventBus).publish(any());
	}

	@Test
	void should_create_subscription_when_event_is_received() {
		// Arrange
		Tenant tenant = TenantMother.random();
		Plan plan = PlanMother.random();
		String subscriptionId = java.util.UUID.randomUUID().toString();
		String billingIntervalValue = "MONTHLY";
		int pricingValue = 50;
		String currencyValue = "USD";
		String sourceValue = "backoffice";
		boolean autoRenewValue = false;

		AddSubscriptionCommand command = AddSubscriptionCommandMother.create(
			subscriptionId,
			tenant.id().value(),
			plan.id().value(),
			billingIntervalValue,
			pricingValue,
			currencyValue,
			null,
			sourceValue,
			autoRenewValue
		);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenant));
		when(planRepository.find(plan.id())).thenReturn(Optional.of(plan));

		// Act
		handler.handle(command);

		// Assert
		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<DomainEvent>> captor = ArgumentCaptor.forClass(List.class);
		verify(eventBus).publish(captor.capture());
		List<DomainEvent> capturedEvents = captor.getValue();
		assertEquals(1, capturedEvents.size());
		DomainEvent capturedEvent = capturedEvents.getFirst();
		assertInstanceOf(TenantSubscribeToPlanEvent.class, capturedEvent);
		TenantSubscribeToPlanEvent tenantSubscribeToPlanEvent = (TenantSubscribeToPlanEvent) capturedEvent;
		assertEquals(tenant.id().value(), tenantSubscribeToPlanEvent.aggregateId());
		Map<String, Serializable> body = capturedEvent.toPrimitives();
		assertEquals(command.billingInterval(), body.get("interval"));
		assertEquals(command.pricing(), body.get("pricing"));
	}

	@Test
	void should_throw_exception_when_tenant_not_found() {
		// Arrange
		AddSubscriptionCommand command = AddSubscriptionCommandMother.random(TenantMother.random().id());
		when(repository.find(any())).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}

	@Test
	void should_throw_exception_when_plan_not_found() {
		// Arrange
		Tenant tenant = TenantMother.random();
		AddSubscriptionCommand command = AddSubscriptionCommandMother.random(tenant.id());

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenant));
		when(planRepository.find(any())).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}

	@Test
	void should_throw_exception_when_tenant_already_has_active_subscription() {
		// Arrange
		Tenant tenant = TenantMother.random();
		Plan plan = PlanMother.random();

		// First subscription
		String subscriptionId1 = java.util.UUID.randomUUID().toString();
		AddSubscriptionCommand command1 = AddSubscriptionCommandMother.create(
			subscriptionId1,
			tenant.id().value(),
			plan.id().value(),
			"monthly",
			100,
			"USD",
			"COUPON123",
			"web",
			true
		);

		// Mock an active subscription
		Tenant tenantWithActiveSubscription = mock(Tenant.class);
		when(tenantWithActiveSubscription.id()).thenReturn(tenant.id());
		when(tenantWithActiveSubscription.subscribeToPlan(any(), any(), any(), any(), any(), any(), any(), any()))
			.thenThrow(new ActiveSubscriptionAlreadyExistsException());

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithActiveSubscription));
		when(planRepository.find(plan.id())).thenReturn(Optional.of(plan));

		// Act & Assert
		assertThrows(ActiveSubscriptionAlreadyExistsException.class, () -> handler.handle(command1));
	}

	@Test
	void should_throw_exception_when_tenant_has_pending_subscription() {
		// Arrange
		Tenant tenant = TenantMother.random();
		Plan plan = PlanMother.random();

		// First subscription
		String subscriptionId1 = java.util.UUID.randomUUID().toString();
		AddSubscriptionCommand command1 = AddSubscriptionCommandMother.create(
			subscriptionId1,
			tenant.id().value(),
			plan.id().value(),
			"monthly",
			100,
			"USD",
			"COUPON123",
			"web",
			true
		);

		// Mock a pending subscription
		Tenant tenantWithPendingSubscription = mock(Tenant.class);
		when(tenantWithPendingSubscription.id()).thenReturn(tenant.id());
		when(tenantWithPendingSubscription.subscribeToPlan(any(), any(), any(), any(), any(), any(), any(), any()))
			.thenThrow(new PendingSubscriptionExistsException());

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithPendingSubscription));
		when(planRepository.find(plan.id())).thenReturn(Optional.of(plan));

		// Act & Assert
		assertThrows(PendingSubscriptionExistsException.class, () -> handler.handle(command1));
	}
}
