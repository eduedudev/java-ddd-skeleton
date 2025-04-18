package com.jaapec.tenant.tenant.domain;

public final class TenantStatusMother {

	public static TenantStatus create(String status) {
		return new TenantStatus(status);
	}

	public static TenantStatus random() {
		return create("ACTIVE");
	}
}
