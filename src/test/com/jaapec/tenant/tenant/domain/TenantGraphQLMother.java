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

	public static String searchTenantsQuery() {
		@Language("Graphql")
		String searchTenantsQuery =
			"""
			query searchTenantsQuery($filters: [FilterInput!]!, $limit: Int, $offset: Int, $orderBy: String, $orderType: OrderType) {
					searchTenants(
					  filters: $filters
					  orderBy: $orderBy
					  orderType: $orderType
					  limit: $limit
					  offset: $offset
					) {
					  data {
						id
						name
						ownerId
					  }
					  pagination {
						currentPage
						totalPages
						totalItems
						hasNext
						hasPrevious
					  }
					}
				  }
			""";
		return searchTenantsQuery;
	}
}
