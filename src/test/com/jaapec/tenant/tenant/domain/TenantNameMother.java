package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.NameMother;

public final class TenantNameMother {

	public static TenantName create(String string) {
		return new TenantName(string);
	}

	public static TenantName random() {
		return create(NameMother.random());
	}
}
