package com.jaapec.tenant.tenant.application.activation_subscription;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.value_objects.BillingInterval;
import com.jaapec.tenant.plans.domain.value_objects.Currency;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.UuidMother;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.application.activate_subscription.ActivateSubscriptionCommand;
import com.jaapec.tenant.tenant.application.activate_subscription.ActivateSubscriptionCommandHandler;
import com.jaapec.tenant.tenant.application.activate_subscription.SubscriptionActivator;
import com.jaapec.tenant.tenant.domain.SubscriptionAlreadyActive;
import com.jaapec.tenant.tenant.domain.SubscriptionNotFound;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;

final class ActivateSubscriptionCommandHandlerShould extends TenantModuleUnitTestCase {

	private ActivateSubscriptionCommandHandler handler;
	private static final int GRACE_PERIOD_DAYS = 5; // Same as in Tenant.java

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
		handler = new ActivateSubscriptionCommandHandler(new SubscriptionActivator(repository, eventBus));
	}

	@Test
	void should_activate_subscription_when_command_is_valid() {
		// Arrange
		Tenant tenant = TenantMother.random();
		Plan plan = PlanMother.random();
		SubscriptionId subscriptionId = new SubscriptionId(UuidMother.generate());

		// Create a tenant with an inactive subscription
		Tenant tenantWithSubscription = tenant.subscribeToPlan(
			subscriptionId,
			plan,
			new BillingInterval(BillingInterval.intervals.MONTHLY.toString()),
			new SubscriptionPricing(2596),
			new Currency(Currency.currency.USD.toString()),
			null,
			new SubscriptionSource(SubscriptionSource.source.BACKOFFICE.toString()),
			new SubscriptionAutoRenew(false)
		);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithSubscription));

		ActivateSubscriptionCommand command = ActivateSubscriptionCommandMother.create(
			tenant.id().value(),
			subscriptionId.value(),
			"credit_card",
			"payment_ref_123"
		);

		// Act
		handler.handle(command);

		// Assert
		ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
		verify(repository).update(tenantCaptor.capture());

		Tenant capturedTenant = tenantCaptor.getValue();
		assertEquals(tenant.id().value(), capturedTenant.id().value());
		assertEquals(subscriptionId.value(), capturedTenant.activeSubscriptionId().value());

		// Verify that the subscription is now active
		TenantPlanSubscription activatedSubscription = capturedTenant
			.subscriptions()
			.stream()
			.filter(s -> s.id().equals(subscriptionId))
			.findFirst()
			.orElseThrow();

		assertTrue(activatedSubscription.isActive());
		assertEquals("credit_card", activatedSubscription.paymentMethod().value());
		assertEquals("payment_ref_123", activatedSubscription.paymentReference().value());

		verify(eventBus).publish(any());
	}

	@Test
	void should_throw_exception_when_tenant_not_found() {
		// Arrange
		ActivateSubscriptionCommand command = ActivateSubscriptionCommandMother.random();
		when(repository.find(any())).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}

	@Test
	void should_throw_exception_when_subscription_already_active() {
		// Arrange
		Tenant tenant = TenantMother.random();
		Plan plan = PlanMother.random();
		SubscriptionId subscriptionId = new SubscriptionId(UuidMother.generate());

		// Create a tenant with a subscription
		Tenant tenantWithSubscription = tenant.subscribeToPlan(
			subscriptionId,
			plan,
			new BillingInterval(BillingInterval.intervals.MONTHLY.toString()),
			new SubscriptionPricing(2596),
			new Currency(Currency.currency.USD.toString()),
			null,
			new SubscriptionSource(SubscriptionSource.source.BACKOFFICE.toString()),
			new SubscriptionAutoRenew(false)
		);

		// Activate the subscription
		Tenant tenantWithActiveSubscription = tenantWithSubscription.activateSubscription(
			subscriptionId,
			new SubscriptionPaymentMethod("test"),
			new SubscriptionPaymentReference("12312")
		);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithActiveSubscription));

		ActivateSubscriptionCommand command = ActivateSubscriptionCommandMother.create(
			tenant.id().value(),
			subscriptionId.value(),
			"credit_card",
			"payment_ref_123"
		);

		// Act & Assert
		assertThrows(SubscriptionAlreadyActive.class, () -> handler.handle(command));
	}

	@Test
	void should_throw_exception_when_subscription_not_found() {
		// Arrange
		Tenant tenant = TenantMother.random();

		// Create a mock tenant that will throw SubscriptionNotFound when activating a non-existent subscription
		Tenant mockTenant = mock(Tenant.class);
		when(mockTenant.id()).thenReturn(tenant.id());

		// Create a non-existent subscription ID
		SubscriptionId nonExistentSubscriptionId = new SubscriptionId(UuidMother.generate());

		// Mock the activateSubscription method to throw SubscriptionNotFound
		when(mockTenant.activateSubscription(eq(nonExistentSubscriptionId), any(), any()))
			.thenThrow(new SubscriptionNotFound());

		when(repository.find(tenant.id())).thenReturn(Optional.of(mockTenant));

		ActivateSubscriptionCommand command = ActivateSubscriptionCommandMother.create(
			tenant.id().value(),
			nonExistentSubscriptionId.value(),
			"credit_card",
			"payment_ref_123"
		);

		// Act & Assert
		assertThrows(SubscriptionNotFound.class, () -> handler.handle(command));
	}

	@Test
	void should_set_start_date_to_now_when_no_previous_subscriptions() {
		// Arrange
		Tenant tenant = TenantMother.random();
		Plan plan = PlanMother.random();
		SubscriptionId subscriptionId = new SubscriptionId(UuidMother.generate());

		// Create a tenant with an inactive subscription (first subscription)
		Tenant tenantWithSubscription = tenant.subscribeToPlan(
			subscriptionId,
			plan,
			new BillingInterval(BillingInterval.intervals.MONTHLY.toString()),
			new SubscriptionPricing(2596),
			new Currency(Currency.currency.USD.toString()),
			null,
			new SubscriptionSource(SubscriptionSource.source.BACKOFFICE.toString()),
			new SubscriptionAutoRenew(false)
		);

		when(repository.find(tenant.id())).thenReturn(Optional.of(tenantWithSubscription));

		ActivateSubscriptionCommand command = ActivateSubscriptionCommandMother.create(
			tenant.id().value(),
			subscriptionId.value(),
			"credit_card",
			"payment_ref_123"
		);

		// Act
		LocalDateTime beforeActivation = LocalDateTime.now();
		handler.handle(command);
		LocalDateTime afterActivation = LocalDateTime.now();

		// Assert
		ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
		verify(repository).update(tenantCaptor.capture());

		Tenant capturedTenant = tenantCaptor.getValue();
		TenantPlanSubscription activatedSubscription = capturedTenant
			.subscriptions()
			.stream()
			.filter(s -> s.id().equals(subscriptionId))
			.findFirst()
			.orElseThrow();

		// The start date should be around the current time (with a 5-second buffer)
		LocalDateTime startDate = activatedSubscription.initDate().valueAsDateTime();
		LocalDateTime bufferBefore = beforeActivation.minusSeconds(5);
		LocalDateTime bufferAfter = afterActivation.plusSeconds(5);
		assertTrue(
			!startDate.isBefore(bufferBefore) && !startDate.isAfter(bufferAfter),
			"Start date should be around the current time (within 5 seconds)"
		);

		verify(eventBus).publish(any());
	}

	@Test
	void should_set_start_date_to_previous_expiration_when_within_grace_period() {
		// Arrange
		Tenant tenant = TenantMother.random();

		// Create a mock tenant with an expired subscription that's within the grace period
		// and a pending second subscription
		Tenant mockTenant = mock(Tenant.class);

		// First subscription (expired but within grace period)
		SubscriptionId firstSubscriptionId = new SubscriptionId(UuidMother.generate());
		TenantPlanSubscription mockExpiredSubscription = mock(TenantPlanSubscription.class);
		SubscriptionExpirationDate mockExpirationDate = mock(SubscriptionExpirationDate.class);

		// Set up the mock to return a date that's within the grace period (2 days ago)
		LocalDateTime expirationDate = LocalDateTime.now().minusDays(2);
		when(mockExpirationDate.valueAsDateTime()).thenReturn(expirationDate);
		when(mockExpiredSubscription.expirationDate()).thenReturn(mockExpirationDate);
		when(mockExpiredSubscription.status()).thenReturn(new SubscriptionStatus(SubscriptionStatus.status.EXPIRED.name()));
		when(mockExpiredSubscription.id()).thenReturn(firstSubscriptionId);

		// Second subscription (pending)
		SubscriptionId secondSubscriptionId = new SubscriptionId(UuidMother.generate());
		TenantPlanSubscription mockPendingSubscription = mock(TenantPlanSubscription.class);
		when(mockPendingSubscription.id()).thenReturn(secondSubscriptionId);
		when(mockPendingSubscription.status())
			.thenReturn(new SubscriptionStatus(SubscriptionStatus.status.INACTIVE.name()));
		when(mockPendingSubscription.paymentStatus())
			.thenReturn(new SubscriptionPaymentStatus(SubscriptionPaymentStatus.status.PENDING.name()));

		// Set up the mock tenant
		when(mockTenant.id()).thenReturn(tenant.id());
		when(mockTenant.subscriptions()).thenReturn(List.of(mockExpiredSubscription, mockPendingSubscription));

		// Mock the activateSubscription method to return a new tenant with the activated subscription
		when(mockTenant.activateSubscription(eq(secondSubscriptionId), any(), any()))
			.thenAnswer(invocation -> {
				SubscriptionPaymentMethod paymentMethod = invocation.getArgument(1);
				SubscriptionPaymentReference paymentReference = invocation.getArgument(2);

				// Create a new subscription with the payment details and a start date equal to the expiration date
				TenantPlanSubscription activatedSubscription = mock(TenantPlanSubscription.class);
				SubscriptionInitDate initDate = mock(SubscriptionInitDate.class);
				when(initDate.valueAsDateTime()).thenReturn(expirationDate);

				when(activatedSubscription.id()).thenReturn(secondSubscriptionId);
				when(activatedSubscription.isActive()).thenReturn(true);
				when(activatedSubscription.paymentMethod()).thenReturn(paymentMethod);
				when(activatedSubscription.paymentReference()).thenReturn(paymentReference);
				when(activatedSubscription.initDate()).thenReturn(initDate);

				// Create a new tenant with the activated subscription
				Tenant newTenant = mock(Tenant.class);
				when(newTenant.id()).thenReturn(tenant.id());
				when(newTenant.activeSubscriptionId()).thenReturn(secondSubscriptionId);
				when(newTenant.subscriptions()).thenReturn(List.of(mockExpiredSubscription, activatedSubscription));

				return newTenant;
			});

		when(repository.find(tenant.id())).thenReturn(Optional.of(mockTenant));

		ActivateSubscriptionCommand command = ActivateSubscriptionCommandMother.create(
			tenant.id().value(),
			secondSubscriptionId.value(),
			"credit_card",
			"payment_ref_123"
		);

		// Act
		handler.handle(command);

		// Assert
		ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
		verify(repository).update(tenantCaptor.capture());

		Tenant capturedTenant = tenantCaptor.getValue();
		TenantPlanSubscription activatedSubscription = capturedTenant
			.subscriptions()
			.stream()
			.filter(s -> s.id().equals(secondSubscriptionId))
			.findFirst()
			.orElseThrow();

		// The start date should be the expiration date of the first subscription
		LocalDateTime startDate = activatedSubscription.initDate().valueAsDateTime();
		assertEquals(
			expirationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			"Start date should be the expiration date of the previous subscription"
		);

		verify(eventBus).publish(any());
	}

	@Test
	void should_set_start_date_to_now_when_outside_grace_period() {
		// Arrange
		Tenant tenant = TenantMother.random();

		// Create a mock tenant with an expired subscription that's outside the grace period
		// and a pending second subscription
		Tenant mockTenant = mock(Tenant.class);

		// First subscription (expired and outside grace period)
		SubscriptionId firstSubscriptionId = new SubscriptionId(UuidMother.generate());
		TenantPlanSubscription mockExpiredSubscription = mock(TenantPlanSubscription.class);
		SubscriptionExpirationDate mockExpirationDate = mock(SubscriptionExpirationDate.class);

		// Set up the mock to return a date that's GRACE_PERIOD_DAYS + 1 days in the past
		LocalDateTime pastDate = LocalDateTime.now().minusDays(GRACE_PERIOD_DAYS + 1);
		when(mockExpirationDate.valueAsDateTime()).thenReturn(pastDate);
		when(mockExpiredSubscription.expirationDate()).thenReturn(mockExpirationDate);
		when(mockExpiredSubscription.status()).thenReturn(new SubscriptionStatus(SubscriptionStatus.status.EXPIRED.name()));
		when(mockExpiredSubscription.id()).thenReturn(firstSubscriptionId);

		// Second subscription (pending)
		SubscriptionId secondSubscriptionId = new SubscriptionId(UuidMother.generate());
		TenantPlanSubscription mockPendingSubscription = mock(TenantPlanSubscription.class);
		when(mockPendingSubscription.id()).thenReturn(secondSubscriptionId);
		when(mockPendingSubscription.status())
			.thenReturn(new SubscriptionStatus(SubscriptionStatus.status.INACTIVE.name()));
		when(mockPendingSubscription.paymentStatus())
			.thenReturn(new SubscriptionPaymentStatus(SubscriptionPaymentStatus.status.PENDING.name()));

		// Set up the mock tenant
		when(mockTenant.id()).thenReturn(tenant.id());
		when(mockTenant.subscriptions()).thenReturn(List.of(mockExpiredSubscription, mockPendingSubscription));

		// Mock the activateSubscription method to return a new tenant with the activated subscription
		when(mockTenant.activateSubscription(eq(secondSubscriptionId), any(), any()))
			.thenAnswer(invocation -> {
				SubscriptionPaymentMethod paymentMethod = invocation.getArgument(1);
				SubscriptionPaymentReference paymentReference = invocation.getArgument(2);

				// Create a new subscription with the payment details and a start date of now
				TenantPlanSubscription activatedSubscription = mock(TenantPlanSubscription.class);
				SubscriptionInitDate initDate = mock(SubscriptionInitDate.class);
				LocalDateTime now = LocalDateTime.now();
				when(initDate.valueAsDateTime()).thenReturn(now);

				when(activatedSubscription.id()).thenReturn(secondSubscriptionId);
				when(activatedSubscription.isActive()).thenReturn(true);
				when(activatedSubscription.paymentMethod()).thenReturn(paymentMethod);
				when(activatedSubscription.paymentReference()).thenReturn(paymentReference);
				when(activatedSubscription.initDate()).thenReturn(initDate);

				// Create a new tenant with the activated subscription
				Tenant newTenant = mock(Tenant.class);
				when(newTenant.id()).thenReturn(tenant.id());
				when(newTenant.activeSubscriptionId()).thenReturn(secondSubscriptionId);
				when(newTenant.subscriptions()).thenReturn(List.of(mockExpiredSubscription, activatedSubscription));

				return newTenant;
			});

		when(repository.find(tenant.id())).thenReturn(Optional.of(mockTenant));

		ActivateSubscriptionCommand command = ActivateSubscriptionCommandMother.create(
			tenant.id().value(),
			secondSubscriptionId.value(),
			"credit_card",
			"payment_ref_123"
		);

		// Act
		LocalDateTime beforeActivation = LocalDateTime.now();
		handler.handle(command);
		LocalDateTime afterActivation = LocalDateTime.now();

		// Assert
		ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
		verify(repository).update(tenantCaptor.capture());

		Tenant capturedTenant = tenantCaptor.getValue();
		TenantPlanSubscription activatedSubscription = capturedTenant
			.subscriptions()
			.stream()
			.filter(s -> s.id().equals(secondSubscriptionId))
			.findFirst()
			.orElseThrow();

		// The start date should be between beforeActivation and afterActivation
		LocalDateTime startDate = activatedSubscription.initDate().valueAsDateTime();
		LocalDateTime bufferBefore = beforeActivation.minusSeconds(5);
		LocalDateTime bufferAfter = afterActivation.plusSeconds(5);
		assertTrue(
			!startDate.isBefore(bufferBefore) && !startDate.isAfter(bufferAfter),
			"Start date should be around the current time (within 5 seconds)"
		);

		verify(eventBus).publish(any());
	}
}
