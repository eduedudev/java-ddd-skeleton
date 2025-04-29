package com.jaapec.tenant.tenant.application.check_domain_verification;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;
import com.jaapec.tenant.tenant.domain.TenantId;

@Service
public final class CheckDomainVerificationQueryHandler
	implements QueryHandler<CheckDomainVerificationQuery, DomainVerificationResponse> {

	private final DomainVerifier verifier;

	public CheckDomainVerificationQueryHandler(DomainVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	public DomainVerificationResponse handle(CheckDomainVerificationQuery query) {
		return verifier.verify(new TenantId(query.tenantId()));
	}
}
