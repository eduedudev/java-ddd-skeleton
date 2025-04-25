package com.jaapec.tenant.tenant.domain;

import java.util.Map;

import org.intellij.lang.annotations.Language;

import com.jaapec.tenant.tenant.application.create.CreateTenantCommand;

public final class TenantGraphQLMother {

	public static String createTenantMutation() {
		@Language("GraphQL")
		String createTenantMutation =
			"""
				mutation
				CreateTenant(
					$name: String!
					) {
					createTenant(
						request: {
				      name: $name
				    }
					)
				}
		""";

		return createTenantMutation;
	}

	public static Map<String, Object> fromCommand(CreateTenantCommand command) {
		return Map.of("id", command.id(), "name", command.name(), "ownerId", command.ownerId());
	}
}
