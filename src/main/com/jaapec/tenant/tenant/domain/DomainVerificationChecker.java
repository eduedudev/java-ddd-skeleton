package com.jaapec.tenant.tenant.domain;

public interface DomainVerificationChecker {
	boolean isVerified(Tenant tenant);
}
