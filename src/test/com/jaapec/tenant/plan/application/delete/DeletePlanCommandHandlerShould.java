package com.jaapec.tenant.plan.application.delete;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.delete.DeletePlanCommand;
import com.jaapec.tenant.plans.application.delete.DeletePlanCommandHandler;
import com.jaapec.tenant.plans.application.delete.PlanDeleter;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.events.PlanDeletedDomainEvent;
import com.jaapec.tenant.plans.domain.value_objects.PlanId;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

class DeletePlanCommandHandlerShould extends PlanModuleUnitTestCase {

	private DeletePlanCommandHandler handler;

	@Captor
	private ArgumentCaptor<List<DomainEvent>> eventsCaptor;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new DeletePlanCommandHandler(new PlanDeleter(repository, eventBus));
	}

	@Test
	void delete_a_plan() {
		Plan plan = PlanMother.random();

		when(repository.find(plan.id())).thenReturn(Optional.of(plan));

		DeletePlanCommand command = new DeletePlanCommand(plan.id().value());
		handler.handle(command);

		verify(repository).delete(plan);

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<DomainEvent>> captor = ArgumentCaptor.forClass(List.class);

		verify(eventBus).publish(captor.capture());

		List<DomainEvent> capturedEvents = captor.getValue();

		assertEquals(1, capturedEvents.size());

		DomainEvent capturedEvent = capturedEvents.getFirst();
		assertInstanceOf(PlanDeletedDomainEvent.class, capturedEvent);

		PlanDeletedDomainEvent planDeletedEvent = (PlanDeletedDomainEvent) capturedEvent;
		assertEquals(plan.id().toString(), planDeletedEvent.aggregateId());
	}

	@Test
	void notDeleteAPlanIfDoesNotExist() {
		String nonExistentPlanId = PlanIdMother.random().value();

		when(repository.find(new PlanId(nonExistentPlanId))).thenReturn(Optional.empty());

		DeletePlanCommand command = new DeletePlanCommand(nonExistentPlanId);

		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}
}
