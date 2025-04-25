package com.jaapec.tenant.plan.application.create;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.domain.*;
import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.plans.application.create.CreatePlanCommandHandler;
import com.jaapec.tenant.plans.application.create.PlanCreator;
import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.events.PlanCreatedDomainEvent;
import com.jaapec.tenant.plans.domain.value_objects.PlanMaxUsers;
import com.jaapec.tenant.plans.domain.value_objects.PlanPriceMonthly;

final class CreatePlanCommandHandlerShould extends PlanModuleUnitTestCase {

	private CreatePlanCommandHandler handler;

	@Override
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

	@Test
	void throw_exception_when_plan_price_monthly_is_negative() {
		BigDecimal negativePrice = BigDecimal.valueOf(-1);
		assertThrows(NonNegativeNumberException.class, () -> new PlanPriceMonthly(negativePrice));
	}

	@Test
	void throw_exception_when_plan_max_users_is_below_minimum() {
		assertThrows(MinValueException.class, () -> new PlanMaxUsers(-1));
	}

	@Test
	void verify_monthly_plan_price_equality() {
		assertEquals(PlanPriceMonthlyMother.create("2.9"), PlanPriceMonthlyMother.create("2.90"));
		assertEquals(PlanPriceMonthlyMother.create("0.00"), PlanPriceMonthlyMother.create("0"));
		assertEquals(PlanPriceMonthlyMother.create("10.00"), PlanPriceMonthlyMother.create("10"));
		assertEquals(PlanPriceMonthlyMother.create("99.9"), PlanPriceMonthlyMother.create("99.90"));
		assertEquals(PlanPriceMonthlyMother.create("99.98"), PlanPriceMonthlyMother.create("99.98"));
		assertNotEquals(PlanPriceMonthlyMother.create("2.9"), PlanPriceMonthlyMother.create("3.0"));
	}
}
