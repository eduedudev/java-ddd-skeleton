package com.jaapec.tenant.tenant.infrastructure.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.tenant.TenantModuleInfrastructureTestCase;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantIdMother;
import com.jaapec.tenant.tenant.domain.TenantMother;

@Transactional
class MariaDBTenantRepositoryShould extends TenantModuleInfrastructureTestCase {

	@Test
	void save_a_tenant() {
		Tenant tenant = TenantMother.random();
		assertDoesNotThrow(() -> mariadbTenantRepository.save(tenant));
	}

	@Test
	void return_an_existing_tenant() {
		Tenant tenant = TenantMother.random();

		mariadbTenantRepository.save(tenant);

		assertEquals(Optional.of(tenant), mariadbTenantRepository.find(tenant.id()));
	}

	@Test
	void not_return_a_non_existing_tenant() {
		assertFalse(mariadbTenantRepository.find(TenantIdMother.random()).isPresent());
	}

	@Test
	void not_allow_duplicate_tenant_ids() {
		Tenant tenant = TenantMother.random();
		mariadbTenantRepository.save(tenant);

		assertThrows(Exception.class, () -> mariadbTenantRepository.save(tenant));
	}

	@Test
	void delete_an_existing_tenant() {
		Tenant tenant = TenantMother.random();
		mariadbTenantRepository.save(tenant);

		assertDoesNotThrow(() -> mariadbTenantRepository.delete(tenant));

		assertFalse(mariadbTenantRepository.find(tenant.id()).isPresent());
	}
}
