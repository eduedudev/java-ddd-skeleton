package com.jaapec.tenant.tenant.application.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.domain.*;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;

class CreateTenantCommandHandlerShould extends TenantModuleUnitTestCase {

	private CreateTenantCommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new CreateTenantCommandHandler(new TenantCreator(repository, eventBus));
	}

	@Test
	void create_a_valid_tenant() {
		CreateTenantCommand command = CreateTenantCommandMother.random();

		Tenant tenant = TenantMother.fromRequest(command);
		TenantCreatedDomainEvent domainEvent = TenantCreatedDomainEventMother.fromTenant(tenant);

		handler.handle(command);

		shouldHaveSaved(tenant);
		shouldHavePublished(domainEvent);
	}

	@Test
	void publish_event_when_tenant_is_created() {
		CreateTenantCommand command = CreateTenantCommandMother.random();
		Tenant tenant = TenantMother.fromRequest(command);

		handler.handle(command);

		shouldHavePublished(TenantCreatedDomainEventMother.fromTenant(tenant));
	}
}
