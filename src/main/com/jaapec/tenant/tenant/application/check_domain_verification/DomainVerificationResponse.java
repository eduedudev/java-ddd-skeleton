package com.jaapec.tenant.tenant.application.check_domain_verification;

import com.jaapec.tenant.shared.domain.bus.query.Response;
import com.jaapec.tenant.tenant.domain.Tenant;

public record DomainVerificationResponse(String domain, boolean domainVerified) implements Response {
	public static DomainVerificationResponse fromAggregate(Tenant tenant) {
		String domainValue = tenant.domain() != null ? tenant.domain().value() : null;
		boolean domainVerifiedValue = Boolean.TRUE.equals(
			tenant.domainVerified() != null ? tenant.domainVerified().value() : false
		);
		return new DomainVerificationResponse(domainValue, domainVerifiedValue);
	}
}
