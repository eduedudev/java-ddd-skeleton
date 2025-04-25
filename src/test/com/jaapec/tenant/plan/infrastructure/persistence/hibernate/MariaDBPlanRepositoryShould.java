package com.jaapec.tenant.plan.infrastructure.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

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
		assertDoesNotThrow(() -> mariadbPlanRepository.save(plan));
	}

	@Test
	void return_an_existing_plan() {
		Plan plan = PlanMother.random();

		mariadbPlanRepository.save(plan);

		Optional<Plan> foundPlan = mariadbPlanRepository.find(plan.id());
		assertTrue(foundPlan.isPresent());

		assertEquals(plan.id(), foundPlan.get().id());
		assertEquals(plan.name(), foundPlan.get().name());
		assertEquals(plan.description(), foundPlan.get().description());
		assertEquals(plan.priceMonthly(), foundPlan.get().priceMonthly());
		assertEquals(plan.priceYearly(), foundPlan.get().priceYearly());
		assertEquals(plan.maxUsers(), foundPlan.get().maxUsers());
		assertEquals(plan.maxRoles(), foundPlan.get().maxRoles());
		assertEquals(plan.maxAccounts(), foundPlan.get().maxAccounts());
		assertEquals(plan.maxInvoices(), foundPlan.get().maxInvoices());
		assertEquals(plan.status(), foundPlan.get().status());
		assertEquals(plan.visibility(), foundPlan.get().visibility());
		assertEquals(plan.trialDays(), foundPlan.get().trialDays());
	}

	@Test
	void not_return_a_non_existing_plan() {
		assertFalse(mariadbPlanRepository.find(PlanIdMother.random()).isPresent());
	}

	@Test
	void not_allow_duplicate_plan_ids() {
		Plan plan = PlanMother.random();
		mariadbPlanRepository.save(plan);

		assertThrows(Exception.class, () -> mariadbPlanRepository.save(plan));
	}

	@Test
	void delete_an_existing_plan() {
		Plan plan = PlanMother.random();
		mariadbPlanRepository.save(plan);

		assertDoesNotThrow(() -> mariadbPlanRepository.delete(plan));

		assertFalse(mariadbPlanRepository.find(plan.id()).isPresent());
	}
}
