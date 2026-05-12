package com.jaapec.tenant.subscription.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.value_objects.BillingInterval;
import com.jaapec.tenant.plans.domain.value_objects.Currency;
import com.jaapec.tenant.subscription.SubscriptionModuleUnitTestCase;
import com.jaapec.tenant.tenant.domain.Tenant;

class TenantPlanSubscriptionShould extends SubscriptionModuleUnitTestCase {

	@Test
	void create_a_new_subscription_with_inactive_status() {
		// Arrange
		Tenant tenant = Mockito.mock(Tenant.class);
		Plan plan = Mockito.mock(Plan.class);
		BillingInterval billingInterval = Mockito.mock(BillingInterval.class);
		Currency currency = Mockito.mock(Currency.class);

		// Act
		TenantPlanSubscription subscription = TenantPlanSubscription.create(
			subscriptionId(),
			tenant,
			plan,
			billingInterval,
			pricing(),
			currency,
			coupon(),
			source(),
			autoRenewEnabled()
		);

		// Assert
		assertEquals(subscriptionId(), subscription.id());
		assertEquals(tenant, subscription.tenant());
		assertEquals(plan, subscription.plan());
		assertEquals(billingInterval, subscription.billingInterval());
		assertEquals(SubscriptionStatus.status.INACTIVE.name(), subscription.status().value());
		assertEquals(pricing(), subscription.pricing());
		assertEquals(currency, subscription.currency());
		assertEquals(coupon(), subscription.coupon());
		assertEquals(source(), subscription.source());
		assertEquals(SubscriptionPaymentStatus.status.PENDING.name(), subscription.paymentStatus().value());
		assertEquals(autoRenewEnabled(), subscription.autoRenew());
		assertNotNull(subscription.createdAt());
		assertNotNull(subscription.updatedAt());
		assertNull(subscription.dateSubscribed());
		assertNull(subscription.initDate());
		assertNull(subscription.expirationDate());
		assertNull(subscription.paymentMethod());
		assertNull(subscription.paymentReference());
		assertFalse(subscription.isActive());
	}

	@Test
	void make_payment_and_activate_subscription() {
		// Arrange
		Tenant tenant = Mockito.mock(Tenant.class);
		Plan plan = Mockito.mock(Plan.class);
		BillingInterval billingInterval = Mockito.mock(BillingInterval.class);
		Mockito.when(billingInterval.value()).thenReturn("MONTHLY");
		Mockito
			.when(billingInterval.calculateExpiration(Mockito.any()))
			.thenAnswer(inv -> ((java.time.LocalDateTime) inv.getArgument(0)).plusMonths(1));
		Currency currency = Mockito.mock(Currency.class);

		TenantPlanSubscription subscription = new TenantPlanSubscription(
			subscriptionId(),
			tenant,
			plan,
			billingInterval,
			inactiveStatus(),
			null,
			null,
			null,
			pricing(),
			currency,
			coupon(),
			source(),
			pendingStatus(),
			null,
			null,
			autoRenewEnabled(),
			createdAt(),
			updatedAt()
		);

		// Act
		TenantPlanSubscription activatedSubscription = subscription.makePayment(
			paymentMethod(),
			paymentReference(),
			initDate()
		);

		// Assert
		assertEquals(subscriptionId(), activatedSubscription.id());
		assertEquals(tenant, activatedSubscription.tenant());
		assertEquals(plan, activatedSubscription.plan());
		assertEquals(billingInterval, activatedSubscription.billingInterval());
		assertEquals(SubscriptionStatus.status.ACTIVE.name(), activatedSubscription.status().value());
		assertEquals(pricing(), activatedSubscription.pricing());
		assertEquals(currency, activatedSubscription.currency());
		assertEquals(coupon(), activatedSubscription.coupon());
		assertEquals(source(), activatedSubscription.source());
		assertEquals(SubscriptionPaymentStatus.status.PAID.name(), activatedSubscription.paymentStatus().value());
		assertEquals(autoRenewEnabled(), activatedSubscription.autoRenew());
		assertEquals(createdAt(), activatedSubscription.createdAt());
		assertNotNull(activatedSubscription.updatedAt());
		assertNotNull(activatedSubscription.dateSubscribed());
		assertEquals(initDate(), activatedSubscription.initDate());
		assertNotNull(activatedSubscription.expirationDate());
		assertEquals(paymentMethod(), activatedSubscription.paymentMethod());
		assertEquals(paymentReference(), activatedSubscription.paymentReference());
		assertTrue(activatedSubscription.isActive());
	}

	@Test
	void cancel_auto_renew_for_active_subscription() {
		// Arrange
		Tenant tenant = Mockito.mock(Tenant.class);
		Plan plan = Mockito.mock(Plan.class);
		BillingInterval billingInterval = Mockito.mock(BillingInterval.class);
		Currency currency = Mockito.mock(Currency.class);

		TenantPlanSubscription subscription = new TenantPlanSubscription(
			subscriptionId(),
			tenant,
			plan,
			billingInterval,
			activeStatus(),
			dateSubscribed(),
			initDate(),
			expirationDate(),
			pricing(),
			currency,
			coupon(),
			source(),
			paidStatus(),
			paymentMethod(),
			paymentReference(),
			autoRenewEnabled(),
			createdAt(),
			updatedAt()
		);

		// Act
		TenantPlanSubscription updatedSubscription = subscription.cancelAutoRenew();

		// Assert
		assertEquals(subscriptionId(), updatedSubscription.id());
		assertEquals(tenant, updatedSubscription.tenant());
		assertEquals(plan, updatedSubscription.plan());
		assertEquals(billingInterval, updatedSubscription.billingInterval());
		assertEquals(activeStatus(), updatedSubscription.status());
		assertEquals(pricing(), updatedSubscription.pricing());
		assertEquals(currency, updatedSubscription.currency());
		assertEquals(coupon(), updatedSubscription.coupon());
		assertEquals(source(), updatedSubscription.source());
		assertEquals(paidStatus(), updatedSubscription.paymentStatus());
		assertFalse(updatedSubscription.autoRenew().value());
		assertEquals(createdAt(), updatedSubscription.createdAt());
		assertNotNull(updatedSubscription.updatedAt());
		assertEquals(dateSubscribed(), updatedSubscription.dateSubscribed());
		assertEquals(initDate(), updatedSubscription.initDate());
		assertEquals(expirationDate(), updatedSubscription.expirationDate());
		assertEquals(paymentMethod(), updatedSubscription.paymentMethod());
		assertEquals(paymentReference(), updatedSubscription.paymentReference());
		assertTrue(updatedSubscription.isActive());
	}

	@Test
	void throw_exception_when_canceling_auto_renew_for_inactive_subscription() {
		// Arrange
		Tenant tenant = Mockito.mock(Tenant.class);
		Plan plan = Mockito.mock(Plan.class);
		BillingInterval billingInterval = Mockito.mock(BillingInterval.class);
		Currency currency = Mockito.mock(Currency.class);

		TenantPlanSubscription subscription = new TenantPlanSubscription(
			subscriptionId(),
			tenant,
			plan,
			billingInterval,
			inactiveStatus(),
			null,
			null,
			null,
			pricing(),
			currency,
			coupon(),
			source(),
			pendingStatus(),
			null,
			null,
			autoRenewEnabled(),
			createdAt(),
			updatedAt()
		);

		// Act & Assert
		assertThrows(SubscriptionIsInactiveException.class, subscription::cancelAutoRenew);
	}

	@Test
	void check_if_subscription_is_active() {
		// Arrange
		Tenant tenant = Mockito.mock(Tenant.class);
		Plan plan = Mockito.mock(Plan.class);
		BillingInterval billingInterval = Mockito.mock(BillingInterval.class);
		Currency currency = Mockito.mock(Currency.class);

		TenantPlanSubscription activeSubscription = new TenantPlanSubscription(
			subscriptionId(),
			tenant,
			plan,
			billingInterval,
			activeStatus(),
			dateSubscribed(),
			initDate(),
			expirationDate(),
			pricing(),
			currency,
			coupon(),
			source(),
			paidStatus(),
			paymentMethod(),
			paymentReference(),
			autoRenewEnabled(),
			createdAt(),
			updatedAt()
		);

		TenantPlanSubscription inactiveSubscription = new TenantPlanSubscription(
			subscriptionId(),
			tenant,
			plan,
			billingInterval,
			inactiveStatus(),
			null,
			null,
			null,
			pricing(),
			currency,
			coupon(),
			source(),
			pendingStatus(),
			null,
			null,
			autoRenewEnabled(),
			createdAt(),
			updatedAt()
		);

		// Act & Assert
		assertTrue(activeSubscription.isActive());
		assertFalse(inactiveSubscription.isActive());
	}
}
