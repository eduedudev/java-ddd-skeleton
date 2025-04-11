package com.jaapec.tenant.plan.application.change_visibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plan.domain.PlanVisibilityMother;
import com.jaapec.tenant.plans.application.change_visibility.ChangeVisibility;
import com.jaapec.tenant.plans.application.change_visibility.ChangeVisibilityPlanCommand;
import com.jaapec.tenant.plans.application.change_visibility.ChangeVisibilityPlanCommandHandler;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.events.ChangeVisibilityPlanDomainEvent;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

class ChangeVisibilityCommandHandlerShould extends PlanModuleUnitTestCase {

	private ChangeVisibilityPlanCommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
		handler = new ChangeVisibilityPlanCommandHandler(new ChangeVisibility(repository, eventBus));
	}

	@Test
	void change_visibility_of_a_plan() {
		Plan plan = PlanMother.random();
		ChangeVisibilityPlanCommand command = ChangeVisibilityPlanCommandMother.create(
			plan.id().value(),
			PlanVisibilityMother.random().value()
		);

		when(repository.find(plan.id())).thenReturn(Optional.of(plan));

		handler.handle(command);

		ArgumentCaptor<Plan> savedPlanCaptor = ArgumentCaptor.forClass(Plan.class);
		verify(repository).update(savedPlanCaptor.capture());

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<DomainEvent>> captor = ArgumentCaptor.forClass(List.class);
		verify(eventBus).publish(captor.capture());

		var capturedPlan = savedPlanCaptor.getValue();
		assertEquals(command.visibility(), capturedPlan.visibility().value());

		List<DomainEvent> capturedEvents = captor.getValue();
		assertEquals(1, capturedEvents.size());
		DomainEvent capturedEvent = capturedEvents.getFirst();
		assertInstanceOf(ChangeVisibilityPlanDomainEvent.class, capturedEvent);
		ChangeVisibilityPlanDomainEvent planUpdatedEvent = (ChangeVisibilityPlanDomainEvent) capturedEvent;
		assertEquals(plan.id().value(), planUpdatedEvent.aggregateId());
	}
}
