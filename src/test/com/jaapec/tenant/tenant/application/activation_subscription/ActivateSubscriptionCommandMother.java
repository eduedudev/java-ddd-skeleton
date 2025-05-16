package com.jaapec.tenant.tenant.application.activation_subscription;

import java.util.UUID;

import com.jaapec.tenant.subscription.domain.SubscriptionId;
import com.jaapec.tenant.subscription.domain.SubscriptionPaymentMethod;
import com.jaapec.tenant.subscription.domain.SubscriptionPaymentReference;
import com.jaapec.tenant.tenant.application.activate_subscription.ActivateSubscriptionCommand;
import com.jaapec.tenant.tenant.domain.TenantId;

public final class ActivateSubscriptionCommandMother {

	public static ActivateSubscriptionCommand create(
		String tenantId,
		String subscriptionId,
		String paymentMethod,
		String paymentReference
	) {
		return new ActivateSubscriptionCommand(tenantId, subscriptionId, paymentMethod, paymentReference);
	}

	public static ActivateSubscriptionCommand create(
		TenantId tenantId,
		SubscriptionId subscriptionId,
		SubscriptionPaymentMethod paymentMethod,
		SubscriptionPaymentReference paymentReference
	) {
		return new ActivateSubscriptionCommand(
			tenantId.value(),
			subscriptionId.value(),
			paymentMethod.value(),
			paymentReference.value()
		);
	}

	public static ActivateSubscriptionCommand random(TenantId tenantId, SubscriptionId subscriptionId) {
		return create(tenantId.value(), subscriptionId.value(), "credit_card", UUID.randomUUID().toString());
	}

	public static ActivateSubscriptionCommand random() {
		return create(
			UUID.randomUUID().toString(),
			UUID.randomUUID().toString(),
			"credit_card",
			UUID.randomUUID().toString()
		);
	}
}
