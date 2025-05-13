package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.plan.application.create.CreatePlanCommandMother;
import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
class PlanCreateDataFetcherShould extends ApplicationTestCase {

	@Test
	void create_a_valid_plan() throws Exception {
		CreatePlanCommand command = CreatePlanCommandMother.random();

		assertResponse(PlanGraphQLMother.createPlanMutation(), "$.data.createPlan", PlanGraphQLMother.fromCommand(command));
	}

	@Test
	void should_fail_when_creating_plan_with_existing_name() throws Exception {
		CreatePlanCommand command = CreatePlanCommandMother.random();

		assertResponse(PlanGraphQLMother.createPlanMutation(), "$.data.createPlan", PlanGraphQLMother.fromCommand(command));

		assertErrorResponse(
			PlanGraphQLMother.createPlanMutation(),
			PlanGraphQLMother.fromCommand(command),
			String.format("The plan with name %s already exists", command.name()),
			Map.of("code", "E409", "reason", "name", "value", command.name())
		);
	}
}
