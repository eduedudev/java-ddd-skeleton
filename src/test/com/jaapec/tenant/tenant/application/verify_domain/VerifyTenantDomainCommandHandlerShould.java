package com.jaapec.tenant.tenant.application.verify_domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.domain.DomainVerificationChecker;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;

class VerifyTenantDomainCommandHandlerShould extends TenantModuleUnitTestCase {

	private VerifyTenantDomainCommandHandler handler;
	private DomainVerificationChecker checker;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
		checker = Mockito.mock(DomainVerificationChecker.class);
		handler = new VerifyTenantDomainCommandHandler(new TenantDomainVerifier(repository, eventBus, checker));
	}

	@Test
	void verify_domain_and_update_tenant_when_domain_is_valid() {
		Tenant tenant1 = TenantMother.random();
		Tenant tenant = TenantMother.randomWithDomain(tenant1, "example.com");

		shouldSearch(tenant);
		Mockito.when(checker.isVerified(tenant)).thenReturn(true);

		VerifyTenantDomainCommand command = new VerifyTenantDomainCommand(tenant.id().value());

		handler.handle(command);

		ArgumentCaptor<Tenant> captor = ArgumentCaptor.forClass(Tenant.class);
		Mockito.verify(repository).update(captor.capture());
		Tenant updated = captor.getValue();

		assertTrue(updated.domainVerified().value());
		assertEquals(tenant.domain().value(), updated.domain().value());
	}
}
