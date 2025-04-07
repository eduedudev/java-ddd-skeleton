package com.jaapec.tenant.plan.infrastructure.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.plan.PlanModuleInfrastructureTestCase;
import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.domain.Plan;

@Transactional
class MariaDBPlanRepositoryShould extends PlanModuleInfrastructureTestCase {

	@Test
	void save_a_plan() {
		Plan plan = PlanMother.random();

		mariadbPlanRepository.save(plan);
	}

	@Test
	void return_an_existing_plan() {
		Plan plan = PlanMother.random();

		mariadbPlanRepository.save(plan);

		assertEquals(Optional.of(plan), mariadbPlanRepository.find(plan.id()));
	}

	@Test
	void not_return_a_non_existing_plan() {
		assertFalse(mariadbPlanRepository.find(PlanIdMother.random()).isPresent());
	}
}
