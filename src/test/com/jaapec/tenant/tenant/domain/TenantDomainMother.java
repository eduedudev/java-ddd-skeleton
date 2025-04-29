package com.jaapec.tenant.tenant.domain;

public final class TenantDomainMother {

	public static TenantDomain create(String domain) {
		return new TenantDomain(domain);
	}

	public static TenantDomain random() {
		String randomDomain = String.format("tenant%d.example.com", (int) (Math.random() * 1000));
		return new TenantDomain(randomDomain);
	}
}
