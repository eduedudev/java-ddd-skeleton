package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.UuidMother;

public final class TenantIdMother {

	public static TenantId create(String string) {
		return new TenantId(string);
	}

	public static TenantId random() {
		return create(UuidMother.generate());
	}
}
