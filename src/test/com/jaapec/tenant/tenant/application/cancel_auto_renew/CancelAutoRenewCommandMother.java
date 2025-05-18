package com.jaapec.tenant.tenant.application.cancel_auto_renew;

import java.util.UUID;

import com.jaapec.tenant.subscription.domain.SubscriptionId;
import com.jaapec.tenant.tenant.domain.TenantId;

public final class CancelAutoRenewCommandMother {

	public static CancelAutoRenewCommand create(String tenantId, String subscriptionId) {
		return new CancelAutoRenewCommand(tenantId, subscriptionId);
	}

	public static CancelAutoRenewCommand create(TenantId tenantId, SubscriptionId subscriptionId) {
		return new CancelAutoRenewCommand(tenantId.value(), subscriptionId.value());
	}

	public static CancelAutoRenewCommand random() {
		return create(UUID.randomUUID().toString(), UUID.randomUUID().toString());
	}

	public static CancelAutoRenewCommand random(TenantId tenantId) {
		return create(tenantId.value(), UUID.randomUUID().toString());
	}

	public static CancelAutoRenewCommand random(TenantId tenantId, SubscriptionId subscriptionId) {
		return create(tenantId.value(), subscriptionId.value());
	}
}
