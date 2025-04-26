package com.jaapec.tenant.tenant.application;

import java.util.Optional;

import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantCustomDomain;
import com.jaapec.tenant.tenant.domain.TenantMother;

public final class TenantResponseMother {

	public static TenantResponse create(Tenant tenant) {
		return new TenantResponse(
			tenant.id().value(),
			tenant.name().value(),
			tenant.status().value(),
			Optional.ofNullable(tenant.customDomain()).map(TenantCustomDomain::value),
			tenant.ownerId().value(),
			tenant.createdAt().value(),
			tenant.updatedAt().value()
		);
	}

	public static TenantResponse random() {
		return create(TenantMother.random());
	}
}
