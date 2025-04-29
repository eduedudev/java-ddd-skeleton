package com.jaapec.tenant.tenant.application;

import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;

public final class TenantResponseMother {

	public static TenantResponse create(Tenant tenant) {
		String domainValue = tenant.domain() != null ? tenant.domain().value() : null;
		return new TenantResponse(
			tenant.id().value(),
			tenant.name().value(),
			tenant.status().value(),
			domainValue,
			tenant.ownerId().value(),
			tenant.createdAt().value(),
			tenant.updatedAt().value()
		);
	}

	public static TenantResponse random() {
		return create(TenantMother.random());
	}
}
