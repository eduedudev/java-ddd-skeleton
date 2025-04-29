package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;
import com.jaapec.tenant.tenant.application.check_domain_verification.DomainVerificationResponse;
import com.jaapec.tenant.tenant.application.check_domain_verification.DomainVerifier;
import com.jaapec.tenant.tenant.domain.*;

@Transactional
class TenantGraphQLDomainVerificationDataFetcherShould extends ApplicationTestCase {

	@Autowired
	private TenantRepository repository;

	@Autowired
	private DomainVerificationChecker domainVerificationChecker;

	@Test
	void check_domain_verification_and_return_updated_status() throws Exception {
		Tenant tenant1 = TenantMother.random();
		repository.save(tenant1);

		Tenant tenant = TenantMother.randomWithDomain(tenant1, "test.jaapec.com");
		repository.update(tenant);

		CommandBus commandBus = Mockito.mock(CommandBus.class);
		TenantRepository mockRepository = Mockito.mock(TenantRepository.class);

		DomainVerificationChecker domainVerificationChecker = Mockito.mock(DomainVerificationChecker.class);
		Mockito.when(domainVerificationChecker.isVerified(Mockito.any(Tenant.class))).thenReturn(true);

		Tenant updatedTenant = tenant.changeStatusDomainVerified(new TenantDomainVerified(true)); // Crear nuevo Tenant con estado actualizado

		Mockito.when(mockRepository.find(tenant.id())).thenReturn(Optional.of(updatedTenant));

		DomainVerifier domainVerifier = new DomainVerifier(mockRepository, commandBus);

		DomainVerificationResponse response = domainVerifier.verify(tenant.id());

		assertThat(response)
			.hasFieldOrPropertyWithValue("domain", tenant.domain().value())
			.hasFieldOrPropertyWithValue("domainVerified", true);

		Mockito.verify(mockRepository, Mockito.times(1)).find(tenant.id());
		assertTrue(response.domainVerified());
	}
}
