package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.application.create.CreateTenantCommand;
import com.jaapec.tenant.tenant.application.update.UpdateTenantCommand;
import com.jaapec.tenant.tenant.domain.*;

@Transactional
class TenantMutationsDataFetcherShould extends ApplicationTestCase {

	@Autowired
	private TenantRepository repository;

	@Test
	void create_a_valid_tenant() throws Exception {
		CreateTenantCommand command = CreateTenantCommandMother.random();

		assertResponse(
			TenantGraphQLMother.createTenantMutation(),
			"$.data.createTenant",
			TenantGraphQLMother.fromCommand(command)
		);
	}

	@Test
	void should_fail_when_creating_tenant_with_invalid_name() throws Exception {
		CreateTenantCommand command = CreateTenantCommandMother.random();

		Map<String, Object> invalidInput = new HashMap<>(TenantGraphQLMother.fromCommand(command));
		invalidInput.put("name", "");

		assertErrorResponse(
			TenantGraphQLMother.createTenantMutation(),
			invalidInput,
			"The field name should not be empty",
			Map.of("field", "name")
		);
	}

	@Test
	void should_update_existing_tenant_through_graphql_mutation() throws Exception {
		Tenant tenant = TenantMother.random();
		repository.save(tenant);
		UpdateTenantCommand command = new UpdateTenantCommand(tenant.id().value(), "Updated Name");

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", command.id());
		variables.put("name", command.name());
		assertResponse(TenantGraphQLMother.updateTenantMutation(), "$.data.updateTenant", variables);

		Map<String, Object> queryVariables = Map.of("id", tenant.id().value());
		TenantResponse updatedTenant = assertResponseWithBody(
			TenantGraphQLMother.findTenantQuery(),
			"$.data.findTenant",
			queryVariables,
			TenantResponse.class
		);

		assertEquals("Updated Name", updatedTenant.name());
	}
}
