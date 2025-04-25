package com.jaapec.tenant.tenant.application;

import java.util.Optional;

import com.jaapec.tenant.shared.domain.bus.query.Response;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantCustomDomain;

public record TenantResponse(
	String id,
	String name,
	Optional<String> domain,
	String ownerId,
	String createdAt,
	String updatedAt
)
	implements Response {
	public static TenantResponse fromAggregate(Tenant tenant) {
		return new TenantResponse(
			tenant.id().value(),
			tenant.name().value(),
			Optional.ofNullable(tenant.customDomain()).map(TenantCustomDomain::value),
			tenant.ownerId().value(),
			tenant.createdAt().value(),
			tenant.updatedAt().value()
		);
	}
}
