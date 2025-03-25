package com.jaapec.tenant.plan;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.users.infrastructure.UnitTestCase;

public abstract class PlanModuleUnitTestCase extends UnitTestCase {

	protected PlanRepository repository;

	@BeforeEach
	protected void setUp() {
		super.setUp();

		repository = mock(PlanRepository.class);
	}

	public void shouldHaveSaved(Plan plan) {
		verify(repository, atLeastOnce()).save(plan);
	}
}
