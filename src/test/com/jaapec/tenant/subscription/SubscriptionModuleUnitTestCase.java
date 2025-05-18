package com.jaapec.tenant.subscription;

import org.junit.jupiter.api.BeforeEach;

import com.jaapec.tenant.shared.infrastructure.UnitTestCase;
import com.jaapec.tenant.subscription.domain.SubscriptionAutoRenew;
import com.jaapec.tenant.subscription.domain.SubscriptionCoupon;
import com.jaapec.tenant.subscription.domain.SubscriptionCreateAt;
import com.jaapec.tenant.subscription.domain.SubscriptionDateSubscribed;
import com.jaapec.tenant.subscription.domain.SubscriptionExpirationDate;
import com.jaapec.tenant.subscription.domain.SubscriptionId;
import com.jaapec.tenant.subscription.domain.SubscriptionInitDate;
import com.jaapec.tenant.subscription.domain.SubscriptionPaymentMethod;
import com.jaapec.tenant.subscription.domain.SubscriptionPaymentReference;
import com.jaapec.tenant.subscription.domain.SubscriptionPaymentStatus;
import com.jaapec.tenant.subscription.domain.SubscriptionPricing;
import com.jaapec.tenant.subscription.domain.SubscriptionSource;
import com.jaapec.tenant.subscription.domain.SubscriptionStatus;
import com.jaapec.tenant.subscription.domain.SubscriptionUpdateAt;

public abstract class SubscriptionModuleUnitTestCase extends UnitTestCase {

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
	}

	protected SubscriptionId subscriptionId() {
		return new SubscriptionId("123e4567-e89b-12d3-a456-426614174000");
	}

	protected SubscriptionStatus activeStatus() {
		return new SubscriptionStatus(SubscriptionStatus.status.ACTIVE.name());
	}

	protected SubscriptionStatus inactiveStatus() {
		return new SubscriptionStatus(SubscriptionStatus.status.INACTIVE.name());
	}

	protected SubscriptionDateSubscribed dateSubscribed() {
		return new SubscriptionDateSubscribed("2023-01-01 00:00:00");
	}

	protected SubscriptionInitDate initDate() {
		return new SubscriptionInitDate("2023-01-01 00:00:00");
	}

	protected SubscriptionExpirationDate expirationDate() {
		return new SubscriptionExpirationDate("2024-01-01 00:00:00");
	}

	protected SubscriptionPricing pricing() {
		return new SubscriptionPricing(100);
	}

	protected SubscriptionCoupon coupon() {
		return new SubscriptionCoupon("DISCOUNT10");
	}

	protected SubscriptionSource source() {
		return new SubscriptionSource(SubscriptionSource.source.SELF_SERVICE.name());
	}

	protected SubscriptionPaymentStatus paidStatus() {
		return new SubscriptionPaymentStatus(SubscriptionPaymentStatus.status.PAID.name());
	}

	protected SubscriptionPaymentStatus pendingStatus() {
		return new SubscriptionPaymentStatus(SubscriptionPaymentStatus.status.PENDING.name());
	}

	protected SubscriptionPaymentMethod paymentMethod() {
		return new SubscriptionPaymentMethod("CREDIT_CARD");
	}

	protected SubscriptionPaymentReference paymentReference() {
		return new SubscriptionPaymentReference("payment-reference");
	}

	protected SubscriptionAutoRenew autoRenewEnabled() {
		return new SubscriptionAutoRenew(true);
	}

	protected SubscriptionAutoRenew autoRenewDisabled() {
		return new SubscriptionAutoRenew(false);
	}

	protected SubscriptionCreateAt createdAt() {
		return new SubscriptionCreateAt("2023-01-01 00:00:00");
	}

	protected SubscriptionUpdateAt updatedAt() {
		return new SubscriptionUpdateAt("2023-01-01 00:00:00");
	}
}
