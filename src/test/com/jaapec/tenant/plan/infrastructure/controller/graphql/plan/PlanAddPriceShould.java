package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.application.add_price.AddPlanPriceCommand;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
class PlanAddPriceShould extends ApplicationTestCase {

	@Autowired
	private PlanRepository repository;

	public static Map<String, Object> variables(AddPlanPriceCommand command) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("id", command.planId());
		variables.put("billingInterval", command.billingInterval());
		variables.put("amount", command.amount());
		variables.put("currency", command.currency());
		return variables;
	}

	@Test
	void add_price_to_plan_existing() throws Exception {
		Plan plan = PlanMother.random();
		repository.save(plan);
		AddPlanPriceCommand command = new AddPlanPriceCommand(
			plan.id().value(),
			UUID.randomUUID().toString(),
			"MONTHLY",
			5000,
			"USD"
		);

		Map<String, Object> variables = variables(command);

		assertResponse(PlanGraphQLMother.addPlanPriceMutation(), "$.data.addPlanPrice", variables);

		Map<String, Object> queryVariables = Map.of("id", plan.id().value());
		PlanResponse updatedPlan = assertResponseWithBody(
			PlanGraphQLMother.findPlanQuery(),
			"$.data.findPlan",
			queryVariables,
			PlanResponse.class
		);

		assertEquals(1, updatedPlan.prices().size());
		assertEquals("USD", updatedPlan.prices().getFirst().currency());
		assertEquals(5000, updatedPlan.prices().getFirst().amount());
		assertEquals("MONTHLY", updatedPlan.prices().getFirst().billingInterval());
	}

	@Test
	void throws_error_when_plan_does_not_exist() throws Exception {
		AddPlanPriceCommand command = new AddPlanPriceCommand(
			PlanIdMother.random().value(),
			UUID.randomUUID().toString(),
			"MONTHLY",
			5000,
			"USD"
		);

		Map<String, Object> variables = variables(command);

		assertErrorResponse(
			PlanGraphQLMother.addPlanPriceMutation(),
			variables,
			"The plan doesnt exist",
			Map.of("code", "E404", "reason", "plan", "value", command.planId())
		);
	}

	@Test
	void should_fail_when_add_price_to_plan_with_invalid_price() throws Exception {
		AddPlanPriceCommand command = new AddPlanPriceCommand(
			PlanIdMother.random().value(),
			UUID.randomUUID().toString(),
			"MONTHLY",
			-5000,
			"USD"
		);
		Map<String, Object> invalidInput = variables(command);

		assertErrorResponse(
			PlanGraphQLMother.addPlanPriceMutation(),
			invalidInput,
			"Field amount must be less than or equal to 0",
			Map.of("field", "amount")
		);
	}
}
