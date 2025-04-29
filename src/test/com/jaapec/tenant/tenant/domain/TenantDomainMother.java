package com.jaapec.tenant.tenant.domain;

public final class TenantDomainMother {

	public static TenantDomain create(String domain) {
		return new TenantDomain(domain);
	}

	public static TenantDomain random() {
		String randomDomain = "tenant" + (int) (Math.random() * 1000) + ".example.com";
		return new TenantDomain(randomDomain);
	}
}
