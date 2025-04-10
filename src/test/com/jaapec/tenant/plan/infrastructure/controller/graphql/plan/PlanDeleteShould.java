package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import java.util.HashMap;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.delete.DeletePlanCommand;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
class PlanDeleteShould extends ApplicationTestCase {

	@Autowired
	private PlanRepository repository;

	public static Map<String, Object> variables(DeletePlanCommand command) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("id", command.id());
		return variables;
	}

	@Test
	void delete_plan_existing() throws Exception {
		Plan plan = PlanMother.random();
		repository.save(plan);
		DeletePlanCommand command = new DeletePlanCommand(plan.id().value());

		Map<String, Object> variables = variables(command);

		assertResponse(PlanGraphQLMother.deletePlanMutation(), "$.data.deletePlan", variables);
	}

	@Test
	void throws_error_when_plan_does_not_exist() throws Exception {
		DeletePlanCommand command = new DeletePlanCommand(PlanIdMother.random().value());

		Map<String, Object> variables = variables(command);

		assertErrorResponse(
			PlanGraphQLMother.deletePlanMutation(),
			variables,
			"The plan doesnt exist",
			Map.of("code", "E404", "reason", "plan", "value", command.id())
		);
	}
}
