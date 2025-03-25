package com.jaapec.tenant.plan.application.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.domain.PlanCreatedDomainEventMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.plans.application.create.CreatePlanCommandHandler;
import com.jaapec.tenant.plans.application.create.PlanCreator;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.events.PlanCreatedDomainEvent;

public final class CreatePlanCommandHandlerShould extends PlanModuleUnitTestCase {

	private CreatePlanCommandHandler handler;

	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new CreatePlanCommandHandler(new PlanCreator(repository, eventBus));
	}

	@Test
	void create_a_valid_plan() {
		CreatePlanCommand command = CreatePlanCommandMother.random();

		Plan plan = PlanMother.fromRequest(command);
		PlanCreatedDomainEvent domainEvent = PlanCreatedDomainEventMother.fromPlan(plan);

		handler.handle(command);

		shouldHaveSaved(plan);
		shouldHavePublished(domainEvent);
	}
}
