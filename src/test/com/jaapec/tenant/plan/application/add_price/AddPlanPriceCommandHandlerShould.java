package com.jaapec.tenant.plan.application.add_price;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.add_price.AddPlanPriceCommand;
import com.jaapec.tenant.plans.application.add_price.AddPlanPriceCommandHandler;
import com.jaapec.tenant.plans.application.add_price.PlanPriceAdder;
import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.value_objects.Amount;
import com.jaapec.tenant.shared.domain.ResourceNotExist;

final class AddPlanPriceCommandHandlerShould extends PlanModuleUnitTestCase {

	private AddPlanPriceCommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new AddPlanPriceCommandHandler(new PlanPriceAdder(repository, eventBus));
	}

	@Test
	void add_valid_plan_price() {
		Plan plan = PlanMother.random();
		AddPlanPriceCommand command = AddPlanPriceCommandMother.random(plan.id());

		when(repository.find(plan.id())).thenReturn(Optional.of(plan));

		handler.handle(command);

		ArgumentCaptor<Plan> savedPlanCaptor = ArgumentCaptor.forClass(Plan.class);
		verify(repository).update(savedPlanCaptor.capture());
	}

	@Test
	void throw_exception_when_plan_not_found() {
		AddPlanPriceCommand command = AddPlanPriceCommandMother.random(PlanIdMother.random());
		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}

	@Test
	void throw_exception_when_plan_amount_is_negative() {
		Integer negativePrice = -25;
		assertThrows(NonNegativeNumberException.class, () -> new Amount(negativePrice));
	}
}
