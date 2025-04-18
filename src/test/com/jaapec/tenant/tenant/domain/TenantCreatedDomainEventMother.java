package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.CurrentDate;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;

public final class TenantCreatedDomainEventMother {

	public static TenantCreatedDomainEvent create(
		TenantId id,
		TenantName name,
		TenantOwnerId ownerId,
		TenantCreatedAt createdAt,
		TenantUpdatedAt updatedAt
	) {
		return new TenantCreatedDomainEvent(
			id.value(),
			name.value(),
			ownerId.value(),
			createdAt.value(),
			updatedAt.value()
		);
	}

	public static TenantCreatedDomainEvent fromTenant(Tenant tenant) {
		System.out.println(tenant.createdAt().toString());
		System.out.println(tenant.updatedAt().toString());
		return create(tenant.id(), tenant.name(), tenant.ownerId(), tenant.createdAt(), tenant.updatedAt());
	}

	public static TenantCreatedDomainEvent random() {
		final String now = CurrentDate.now();
		return create(
			TenantIdMother.random(),
			TenantNameMother.random(),
			TenantOwnerIdMother.random(),
			new TenantCreatedAt(now),
			new TenantUpdatedAt(now)
		);
	}
}
