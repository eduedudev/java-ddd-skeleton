package com.jaapec.tenant.plan.application.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.application.PlanResponseMother;
import com.jaapec.tenant.plan.application.create.CreatePlanCommandMother;
import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.application.find.FindPlanQuery;
import com.jaapec.tenant.plans.application.find.FindPlanQueryHandler;
import com.jaapec.tenant.plans.application.find.PlanFinder;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.shared.domain.ResourceNotExist;

final class FindPlanQueryHandlerShould extends PlanModuleUnitTestCase {

	private FindPlanQueryHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new FindPlanQueryHandler(new PlanFinder(repository));
	}

	@Test
	void return_plan_when_it_exists() {
		Plan plan = PlanMother.fromRequest(CreatePlanCommandMother.random());
		FindPlanQuery query = new FindPlanQuery(plan.id().value());
		PlanResponse response = PlanResponseMother.create(plan);
		shouldSearch(plan);
		assertEquals(response, handler.handle(query));
	}

	@Test
	void return_not_found_when_plan_does_not_exist() {
		FindPlanQuery query = new FindPlanQuery(PlanIdMother.random().value());
		shouldSearch();
		assertThrows(ResourceNotExist.class, () -> handler.handle(query));
	}
}
