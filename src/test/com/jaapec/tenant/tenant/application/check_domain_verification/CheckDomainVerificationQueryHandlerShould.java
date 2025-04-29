package com.jaapec.tenant.tenant.application.check_domain_verification;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.application.verify_domain.VerifyTenantDomainCommand;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;

final class CheckDomainVerificationQueryHandlerShould extends TenantModuleUnitTestCase {

	private CheckDomainVerificationQueryHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new CheckDomainVerificationQueryHandler(new DomainVerifier(repository, command));
	}

	@Test
	void verify_domain_and_return_response_false_when_domain_is_not_verified() {
		Tenant tenant = TenantMother.random();
		Tenant tenantWithDomain = TenantMother.randomWithDomain(tenant, "test.jaapec.com");
		shouldSearch(tenantWithDomain);

		CheckDomainVerificationQuery query = new CheckDomainVerificationQuery(tenantWithDomain.id().value());

		DomainVerificationResponse result = handler.handle(query);

		assertEquals(tenantWithDomain.domain().value(), result.domain());
		assertFalse(result.domainVerified());
		Mockito.verify(command).dispatch(new VerifyTenantDomainCommand(tenantWithDomain.id().value()));
	}
}
