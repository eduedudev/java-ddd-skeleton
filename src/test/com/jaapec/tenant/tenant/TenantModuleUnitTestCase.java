package com.jaapec.tenant.tenant;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantRepository;
import com.jaapec.tenant.users.infrastructure.UnitTestCase;

public class TenantModuleUnitTestCase extends UnitTestCase {

	protected TenantRepository repository;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		repository = mock(TenantRepository.class);
	}

	public void shouldHaveSaved(Tenant tenant) {
		verify(repository, atLeastOnce()).save(tenant);
	}

	public void shouldSearch(Tenant tenant) {
		Mockito.when(repository.find(tenant.id())).thenReturn(Optional.of(tenant));
	}

	public void shouldSearch() {
		Mockito.when(repository.find(Mockito.any())).thenReturn(Optional.empty());
	}
}
