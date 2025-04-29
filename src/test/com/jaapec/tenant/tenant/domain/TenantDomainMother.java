package com.jaapec.tenant.tenant.domain;

import java.util.Random;

public final class TenantDomainMother {

	public static TenantDomain create(String domain) {
		return new TenantDomain(domain);
	}

	public static TenantDomain random() {
		Random random = new Random();
		String randomDomain = String.format("tenant%d.example.com", random.nextInt(1000));
		return new TenantDomain(randomDomain);
	}
}
