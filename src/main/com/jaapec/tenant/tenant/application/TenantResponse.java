package com.jaapec.tenant.tenant.application;

import com.jaapec.tenant.shared.domain.bus.query.Response;
import com.jaapec.tenant.tenant.domain.Tenant;

public record TenantResponse(
	String id,
	String name,
	String status,
	String domain,
	String ownerId,
	String createdAt,
	String updatedAt
)
	implements Response {
	public static TenantResponse fromAggregate(Tenant tenant) {
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
}
