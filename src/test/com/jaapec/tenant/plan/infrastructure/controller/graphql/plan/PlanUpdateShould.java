package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.application.update.UpdatePlanCommand;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.PlanStatus;
import com.jaapec.tenant.plans.domain.value_objects.PlanVisibility;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
class PlanUpdateShould extends ApplicationTestCase {

	@Autowired
	private PlanRepository repository;

	public static Map<String, Object> variables(UpdatePlanCommand command) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("id", command.id());
		variables.put("name", command.name());
		variables.put("description", command.description());
		variables.put("priceMonthly", command.priceMonthly());
		variables.put("priceYearly", command.priceYearly());
		variables.put("maxUsers", command.maxUsers());
		variables.put("maxRoles", command.maxRoles());
		variables.put("maxAccounts", command.maxAccounts());
		variables.put("maxInvoices", command.maxInvoices());
		variables.put("status", command.status());
		variables.put("visibility", command.visibility());
		variables.put("trialDays", command.trialDays());
		return variables;
	}

	@Test
	void update_plan_existing() throws Exception {
		Plan plan = PlanMother.random();
		repository.save(plan);
		UpdatePlanCommand command = new UpdatePlanCommand(
			plan.id().value(),
			"Updated Name",
			"Updated Description",
			199.99,
			1999.99,
			50,
			10,
			25,
			100,
			"ACTIVE",
			"PRIVATE",
			15
		);

		Map<String, Object> variables = variables(command);

		assertResponse(PlanGraphQLMother.updatePlanMutation(), "$.data.updatePlan", variables);

		Map<String, Object> queryVariables = Map.of("id", plan.id().value());
		PlanResponse updatedPlan = assertResponseWithBody(
			PlanGraphQLMother.findPlanQuery(),
			"$.data.findPlan",
			queryVariables,
			PlanResponse.class
		);

		assertEquals("Updated Name", updatedPlan.name());
		assertEquals("Updated Description", updatedPlan.description());
		assertEquals(199.99, updatedPlan.priceMonthly());
		assertEquals(1999.99, updatedPlan.priceYearly());
		assertEquals(50, updatedPlan.maxUsers());
		assertEquals(10, updatedPlan.maxRoles());
		assertEquals(25, updatedPlan.maxAccounts());
		assertEquals(100, updatedPlan.maxInvoices());
		assertEquals(PlanStatus.status.ACTIVE.toString(), updatedPlan.status());
		assertEquals(PlanVisibility.visibility.PRIVATE.toString(), updatedPlan.visibility());
		assertEquals(15, updatedPlan.trialDays());
	}

	@Test
	void throws_error_when_id_is_missing() throws Exception {
		UpdatePlanCommand command = new UpdatePlanCommand(
			null, // Missing id
			"Name",
			"Description",
			99.99,
			999.99,
			10,
			5,
			20,
			50,
			"ACTIVE",
			"PRIVATE",
			7
		);

		Map<String, Object> variables = variables(command);

		// Execute the mutation and check for error response
		assertErrorResponse(
			PlanGraphQLMother.updatePlanMutation(),
			variables,
			"Variable 'id' has an invalid value: Variable 'id' has coerced Null value for NonNull type 'ID!'",
			Map.of()
		);
	}

	@Test
	void throws_error_when_plan_does_not_exist() throws Exception {
		UpdatePlanCommand command = new UpdatePlanCommand(
			PlanIdMother.random().value(), // Non-existing plan id
			"Non-Existent Plan",
			"Description for non-existent plan",
			99.99,
			999.99,
			10,
			5,
			20,
			50,
			"ACTIVE",
			"PRIVATE",
			7
		);

		Map<String, Object> variables = variables(command);

		assertErrorResponse(
			PlanGraphQLMother.updatePlanMutation(),
			variables,
			"The plan doesnt exist",
			Map.of("code", "E404", "reason", "plan", "value", command.id())
		);
	}
}
