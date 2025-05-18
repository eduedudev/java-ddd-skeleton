package com.jaapec.tenant.subscription.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SubscriptionStatusShould {

	@Test
	void return_true_when_checking_if_status_is_active_and_it_is_active() {
		SubscriptionStatus status = new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name());

		assertTrue(status.isActive());
		assertFalse(status.isExpired());
		assertEquals(SubscriptionStatus.status.ACTIVE.name(), status.value());
	}

	@Test
	void return_false_when_checking_if_status_is_active_and_it_is_inactive() {
		SubscriptionStatus status = new SubscriptionStatus(SubscriptionStatus.status.INACTIVE.name());

		assertFalse(status.isActive());
		assertFalse(status.isExpired());
		assertEquals(SubscriptionStatus.status.INACTIVE.name(), status.value());
	}

	@Test
	void return_true_when_checking_if_status_is_expired_and_it_is_expired() {
		SubscriptionStatus status = new SubscriptionStatus(SubscriptionStatus.status.EXPIRED.name());

		assertFalse(status.isActive());
		assertTrue(status.isExpired());
		assertEquals(SubscriptionStatus.status.EXPIRED.name(), status.value());
	}

	@Test
	void be_equal_to_another_status_with_same_value() {
		SubscriptionStatus status1 = new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name());
		SubscriptionStatus status2 = new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name());

		assertEquals(status1, status2);
		assertEquals(status1.hashCode(), status2.hashCode());
	}

	@Test
	void not_be_equal_to_another_status_with_different_value() {
		SubscriptionStatus status1 = new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name());
		SubscriptionStatus status2 = new SubscriptionStatus(SubscriptionStatus.status.INACTIVE.name());

		assertNotEquals(status1, status2);
		assertNotEquals(status1.hashCode(), status2.hashCode());
	}

	@Test
	void not_be_equal_to_null() {
		SubscriptionStatus status = new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name());

		assertNotEquals(null, status);
	}

	@Test
	void not_be_equal_to_object_of_different_class() {
		SubscriptionStatus status = new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name());

		assertNotEquals(SubscriptionStatus.status.INACTIVE.name(), status.value());
	}
}
