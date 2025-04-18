package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.UuidMother;

public final class TenantOwnerIdMother {

	public static TenantOwnerId create(String string) {
		return new TenantOwnerId(string);
	}

	public static TenantOwnerId random() {
		return create(UuidMother.generate());
	}
}
