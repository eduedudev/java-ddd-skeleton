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

	public static String findTenantQuery() {
		@Language("Graphql")
		String findTenantQuery =
			"""
			query FindTenant
			(
			  $id: String!
			)\s
			{
				findTenant(
			    id:$id
			  ){
				id,
			    name,
			    status,
			    domain,
				ownerId,
				createdAt,
				updatedAt
			  }
			}
			""";
		return findTenantQuery;
	}

	public static String updateTenantMutation() {
		@Language("GraphQL")
		String updateTenantMutation =
			"""
			mutation UpdateTenant(
				$id: ID!,
				$name: String!,\s
			  ) {
				updateTenant(
				  id: $id,
				  request: {
					name: $name,\s
				  }
				)
			  }

		""";
		return updateTenantMutation;
	}

	public static String changeDomainMutation() {
		@Language("GraphQL")
		String changeDomainMutation =
			"""
			mutation ChangeDomain(
				$id: ID!,
				$request: RequestDomainInput!,\s
			  ) {
				changeDomain(
				  id: $id,
				  request: $request
				)
			  }
			""";
		return changeDomainMutation;
	}

	public static String checkDomainVerificationQuery() {
		@Language("Graphql")
		String checkDomainVerificationQuery =
			"""
			query CheckDomainVerification
			(
			  $id: String!
			)\s
			{
				checkDomainVerification(
			    id:$id
			  ){
			    domain,
			    domainVerified
			  }
			}
			""";
		return checkDomainVerificationQuery;
	}

	public static String cancelAutoRenewMutation() {
		@Language("GraphQL")
		String cancelAutoRenewMutation =
			"""
			mutation CancelAutoRenew(
				$tenantId: ID!,
				$subscriptionId: ID!
			) {
				cancelAutoRenew(
					tenantId: $tenantId,
					subscriptionId: $subscriptionId
				)
			}
			""";
		return cancelAutoRenewMutation;
	}
}
