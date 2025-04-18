package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import java.util.HashMap;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;
import com.jaapec.tenant.tenant.application.CreateTenantCommand;
import com.jaapec.tenant.tenant.domain.CreateTenantCommandMother;
import com.jaapec.tenant.tenant.domain.TenantGraphQLMother;

@Transactional
class TenantCreateDataFetcherShould extends ApplicationTestCase {

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
}
