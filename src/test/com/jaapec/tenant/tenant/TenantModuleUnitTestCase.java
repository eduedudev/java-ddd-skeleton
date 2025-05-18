package com.jaapec.tenant.tenant;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.infrastructure.UnitTestCase;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantRepository;

public abstract class TenantModuleUnitTestCase extends UnitTestCase {

	protected TenantRepository repository;
	protected CommandBus command;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		repository = mock(TenantRepository.class);
		command = mock(CommandBus.class);
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
