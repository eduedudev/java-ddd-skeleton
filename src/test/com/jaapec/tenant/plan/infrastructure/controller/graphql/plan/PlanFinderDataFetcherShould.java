package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.HashMap;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
class PlanFinderDataFetcherShould extends ApplicationTestCase {

	@Autowired
	private PlanRepository planRepository;

	@Test
	void return_plan_when_it_exists() throws Exception {
		Plan plan = PlanMother.random();
		planRepository.save(plan);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", plan.id().value());
		PlanResponse foundPlan = assertResponseWithBody(
			PlanGraphQLMother.findPlanQuery(),
			"$.data.findPlan",
			variables,
			PlanResponse.class
		);

		assertThat(foundPlan)
			.hasFieldOrPropertyWithValue("id", plan.id().value())
			.hasFieldOrPropertyWithValue("name", plan.name().value())
			.hasFieldOrPropertyWithValue("description", plan.description().value())
			.hasFieldOrPropertyWithValue("maxUsers", plan.maxUsers().value())
			.hasFieldOrPropertyWithValue("maxRoles", plan.maxRoles().value())
			.hasFieldOrPropertyWithValue("maxAccounts", plan.maxAccounts().value())
			.hasFieldOrPropertyWithValue("maxInvoices", plan.maxInvoices().value())
			.hasFieldOrPropertyWithValue("status", plan.status().value())
			.hasFieldOrPropertyWithValue("visibility", plan.visibility().value())
			.hasFieldOrPropertyWithValue("trialDays", plan.trialDays().value());
	}

	@Test
	void return_fail_when_plan_does_not_exist() throws Exception {
		String id = PlanIdMother.random().value();
		assertErrorResponse(
			PlanGraphQLMother.findPlanQuery(),
			Map.of("id", id),
			"The plan doesnt exist",
			Map.of("code", "E404", "reason", "plan", "value", id)
		);
	}
}
