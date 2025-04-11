package com.jaapec.tenant.plan.application.update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.domain.PlanDescriptionMother;
import com.jaapec.tenant.plan.domain.PlanIdMother;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.update.PlanUpdater;
import com.jaapec.tenant.plans.application.update.UpdatePlanCommand;
import com.jaapec.tenant.plans.application.update.UpdatePlanCommandHandler;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.events.PlanUpdatedDomainEvent;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

final class UpdatePlanCommandHandlerShould extends PlanModuleUnitTestCase {

	private UpdatePlanCommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new UpdatePlanCommandHandler(new PlanUpdater(repository, eventBus));
	}

	@Test
	void update_a_valid_plan() {
		Plan plan = PlanMother.random();
		UpdatePlanCommand command = UpdatePlanCommandMother.random(plan.id());

		when(repository.find(plan.id())).thenReturn(Optional.of(plan));

		handler.handle(command);

		ArgumentCaptor<Plan> savedPlanCaptor = ArgumentCaptor.forClass(Plan.class);
		verify(repository).update(savedPlanCaptor.capture());

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<DomainEvent>> captor = ArgumentCaptor.forClass(List.class);
		verify(eventBus).publish(captor.capture());
		List<DomainEvent> capturedEvents = captor.getValue();
		assertEquals(1, capturedEvents.size());
		DomainEvent capturedEvent = capturedEvents.getFirst();
		assertInstanceOf(PlanUpdatedDomainEvent.class, capturedEvent);
		PlanUpdatedDomainEvent planUpdatedEvent = (PlanUpdatedDomainEvent) capturedEvent;
		assertEquals(plan.id().value(), planUpdatedEvent.aggregateId());
		Map<String, Serializable> body = capturedEvent.toPrimitives();
		assertEquals(command.name(), body.get("name"));
		assertEquals(command.description(), body.get("description"));
		assertEquals(Double.toString(command.priceMonthly()), body.get("priceMonthly"));
		assertEquals(Double.toString(command.priceYearly()), body.get("priceYearly"));
		assertEquals(Integer.toString(command.maxUsers()), body.get("maxUsers"));
		assertEquals(Integer.toString(command.maxRoles()), body.get("maxRoles"));
		assertEquals(Integer.toString(command.maxAccounts()), body.get("maxAccounts"));
		assertEquals(Integer.toString(command.maxInvoices()), body.get("maxInvoices"));
		assertEquals(command.status(), body.get("status"));
		assertEquals(command.visibility(), body.get("visibility"));
		assertEquals(Integer.toString(command.trialDays()), body.get("trialDays"));

		Plan capturedPlan = savedPlanCaptor.getValue();
		assertEquals(command.id(), capturedPlan.id().value());
		assertEquals(command.name(), capturedPlan.name().value());
		assertEquals(command.description(), capturedPlan.description().value());
		assertEquals(command.priceMonthly(), Double.parseDouble(capturedPlan.priceMonthly().value().toString()));
		assertEquals(command.priceYearly(), Double.parseDouble(capturedPlan.priceYearly().value().toString()));
		assertEquals(command.maxUsers(), capturedPlan.maxUsers().value());
		assertEquals(command.maxRoles(), capturedPlan.maxRoles().value());
		assertEquals(command.maxAccounts(), capturedPlan.maxAccounts().value());
		assertEquals(command.maxInvoices(), capturedPlan.maxInvoices().value());
		assertEquals(command.status(), capturedPlan.status().value());
		assertEquals(command.visibility(), capturedPlan.visibility().value());
		assertEquals(command.trialDays(), capturedPlan.trialDays().value());
	}

	@Test
	void keep_original_values_when_command_fields_are_null() {
		Plan originalPlan = PlanMother.random();
		UpdatePlanCommand command = UpdatePlanCommandMother.create(
			originalPlan.id(),
			originalPlan.name(),
			PlanDescriptionMother.create("Description"),
			originalPlan.priceMonthly(),
			originalPlan.priceYearly(),
			originalPlan.maxUsers(),
			originalPlan.maxRoles(),
			originalPlan.maxAccounts(),
			originalPlan.maxInvoices(),
			originalPlan.status(),
			originalPlan.visibility(),
			originalPlan.trialDays()
		);

		when(repository.find(originalPlan.id())).thenReturn(Optional.of(originalPlan));

		handler.handle(command);

		ArgumentCaptor<Plan> savedPlanCaptor = ArgumentCaptor.forClass(Plan.class);
		verify(repository).update(savedPlanCaptor.capture());

		Plan capturedPlan = savedPlanCaptor.getValue();
		assertEquals(originalPlan.id().value(), capturedPlan.id().value());
		assertEquals(originalPlan.name().value(), capturedPlan.name().value());
		assertEquals("Description", capturedPlan.description().value());
		assertEquals(originalPlan.priceMonthly().value(), capturedPlan.priceMonthly().value());
		assertEquals(originalPlan.priceYearly().value(), capturedPlan.priceYearly().value());
		assertEquals(originalPlan.maxUsers().value(), capturedPlan.maxUsers().value());
		assertEquals(originalPlan.maxRoles().value(), capturedPlan.maxRoles().value());
		assertEquals(originalPlan.maxAccounts().value(), capturedPlan.maxAccounts().value());
		assertEquals(originalPlan.maxInvoices().value(), capturedPlan.maxInvoices().value());
		assertEquals(originalPlan.status().value(), capturedPlan.status().value());
		assertEquals(originalPlan.visibility().value(), capturedPlan.visibility().value());
		assertEquals(originalPlan.trialDays().value(), capturedPlan.trialDays().value());
	}

	@Test
	void throw_exception_when_plan_not_found() {
		UpdatePlanCommand command = UpdatePlanCommandMother.random(PlanIdMother.random());
		assertThrows(ResourceNotExist.class, () -> handler.handle(command));
	}
}
