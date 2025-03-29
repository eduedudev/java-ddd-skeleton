package com.jaapec.tenant.plan.infrastructure.controller.graphql;

import java.util.HashMap;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.plan.application.create.CreatePlanCommandMother;
import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
class PlanPostControllerGraphqlShould extends ApplicationTestCase {

	@Test
	void createAValidPlan() throws Exception {
		CreatePlanCommand command = CreatePlanCommandMother.random();

		assertResponse(PlanGraphQLMother.createPlanMutation(), "$.data.createPlan", PlanGraphQLMother.fromCommand(command));
	}

	@Test
	void shouldFailWhenCreatingPlanWithExistingName() throws Exception {
		CreatePlanCommand command = CreatePlanCommandMother.random();

		assertResponse(PlanGraphQLMother.createPlanMutation(), "$.data.createPlan", PlanGraphQLMother.fromCommand(command));

		assertErrorResponse(
			PlanGraphQLMother.createPlanMutation(),
			PlanGraphQLMother.fromCommand(command),
			String.format("The plan with name %s already exists", command.name()),
			Map.of("code", "E409", "reason", "name", "value", command.name())
		);
	}

	@Test
	void shouldFailWhenCreatingPlanWithInvalidPriceMonthly() throws Exception {
		CreatePlanCommand command = CreatePlanCommandMother.random();

		Map<String, Object> invalidInput = new HashMap<>(PlanGraphQLMother.fromCommand(command));
		invalidInput.put("priceMonthly", -1);

		assertErrorResponse(
			PlanGraphQLMother.createPlanMutation(),
			invalidInput,
			"Field priceMonthly must be less than or equal to 0",
			Map.of("field", "priceMonthly")
		);
	}
}
