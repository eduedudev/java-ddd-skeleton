package com.jaapec.tenant.tenant.domain;

import java.util.UUID;

import com.jaapec.tenant.plan.domain.PlanNameMother;
import com.jaapec.tenant.tenant.application.create.CreateTenantCommand;

public final class TenantMother {

	public static Tenant create(TenantId id, TenantName name, TenantOwnerId ownerId) {
		return Tenant.create(id, name, ownerId);
	}

	public static Tenant fromRequest(CreateTenantCommand command) {
		return create(
			TenantIdMother.create(command.id()),
			TenantNameMother.create(command.name()),
			TenantOwnerIdMother.create(command.ownerId())
		);
	}

	public static Tenant random() {
		String name = UUID.randomUUID().toString().substring(0, 8);
		name = name.replaceAll("-", "");
		name = name.concat(PlanNameMother.random().value());
		return create(TenantIdMother.random(), TenantNameMother.create(name), TenantOwnerIdMother.random());
	}

	public static Tenant withName(String name) {
		return create(TenantIdMother.random(), TenantNameMother.create(name), TenantOwnerIdMother.random());
	}

	public static Tenant withNameAndOwnerId(String name, String ownerId) {
		return create(TenantIdMother.random(), TenantNameMother.create(name), TenantOwnerIdMother.create(ownerId));
	}

	public static Tenant randomWithDomain(Tenant tenant, String domain) {
		return tenant.changeDomain(TenantDomainMother.create(domain));
	}
}
