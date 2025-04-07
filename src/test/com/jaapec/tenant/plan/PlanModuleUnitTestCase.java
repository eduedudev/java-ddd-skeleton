package com.jaapec.tenant.plan;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.users.infrastructure.UnitTestCase;

public abstract class PlanModuleUnitTestCase extends UnitTestCase {

	protected PlanRepository repository;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		repository = mock(PlanRepository.class);
	}

	public void shouldHaveSaved(Plan plan) {
		verify(repository, atLeastOnce()).save(plan);
	}

	public void shouldSearch(Plan plan) {
		Mockito.when(repository.find(plan.id())).thenReturn(Optional.of(plan));
	}

	public void shouldSearch() {
		Mockito.when(repository.find(Mockito.any())).thenReturn(Optional.empty());
	}
}
