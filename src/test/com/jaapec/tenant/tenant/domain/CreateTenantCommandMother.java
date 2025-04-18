package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.tenant.application.CreateTenantCommand;

public final class CreateTenantCommandMother {

	public static CreateTenantCommand create(TenantId id, TenantName name, TenantOwnerId ownerId) {
		return new CreateTenantCommand(id.value(), name.value(), ownerId.value());
	}

	public static CreateTenantCommand fromTenant(Tenant tenant) {
		return create(tenant.id(), tenant.name(), tenant.ownerId());
	}

	public static CreateTenantCommand random() {
		return create(TenantIdMother.random(), TenantNameMother.random(), TenantOwnerIdMother.random());
	}
}
