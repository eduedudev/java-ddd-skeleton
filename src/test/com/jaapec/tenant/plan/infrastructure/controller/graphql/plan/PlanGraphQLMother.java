package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import java.util.HashMap;
import java.util.Map;

import org.intellij.lang.annotations.Language;

import com.jaapec.tenant.plans.application.create.CreatePlanCommand;

public final class PlanGraphQLMother {

	public static String createPlanMutation() {
		@Language("GraphQL")
		String createPlanMutation =
			"""
				mutation CreatePlan
				(
				  $name: String!, $description: String!,\s
					$priceMonthly: Float!, $priceYearly: Float!, $maxUsers: Int!,
					$maxRoles: Int!, $maxAccounts: Int!, $maxInvoices: Int!,\s
					$status: PlanStatus!, $visibility: PlanVisibility!, $trialDays: Int!
				)\s
				{
					createPlan(
				    request: {
				      name: $name, description: $description,\s
							priceMonthly: $priceMonthly, priceYearly: $priceYearly, maxUsers: $maxUsers,\s
							maxRoles: $maxRoles, maxAccounts: $maxAccounts, maxInvoices: $maxInvoices,\s
							status: $status, visibility: $visibility, trialDays: $trialDays
				    }
				  )
				 \s
				}
			""";
		return createPlanMutation;
	}

	public static Map<String, Object> fromCommand(CreatePlanCommand command) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("name", command.name());
		variables.put("description", command.description());
		variables.put("priceMonthly", command.priceMonthly());
		variables.put("priceYearly", command.priceYearly());
		variables.put("maxUsers", command.maxUsers());
		variables.put("maxRoles", command.maxRoles());
		variables.put("maxAccounts", command.maxAccounts());
		variables.put("maxInvoices", command.maxInvoices());
		variables.put("status", command.status());
		variables.put("visibility", command.visibility());
		variables.put("trialDays", command.trialDays());
		return variables;
	}

	public static String findPlanQuery() {
		@Language("GraphQL")
		String findPlanQuery =
			"""

				query FindPlan
			(
			  $id: String!
			)\s
			{
				findPlan(
			    id:$id
			  ){
			    id,
			    name,
			    description,
			    priceMonthly,
			    priceYearly,
			    maxUsers,
			    maxRoles,
			    maxAccounts,
			    maxInvoices,
			    status,
			    visibility,
			    trialDays,
			    createdAt,
			    updatedAt
			  }
			}
			""";
		return findPlanQuery;
	}

	public static String searchPlansQuery() {
		@Language("GraphQL")
		String searchPlansQuery =
			"""
				query searchPlansQuery($filters: [FilterInput!]!, $limit: Int, $offset: Int, $orderBy: String, $orderType: OrderType) {
					searchPlans(
					  filters: $filters
					  orderBy: $orderBy
					  orderType: $orderType
					  limit: $limit
					  offset: $offset
					) {
					  data {
						id
						name
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
		return searchPlansQuery;
	}

	public static String updatePlanMutation() {
		@Language("GraphQL")
		String updatePlanMutation =
			"""
			mutation UpdatePlan(
				$id: ID!,
				$name: String!,\s
				$description: String!,
				$priceMonthly: Float!,\s
				$priceYearly: Float!,\s
				$maxUsers: Int!,
				$maxRoles: Int!,\s
				$maxAccounts: Int!,\s
				$maxInvoices: Int!,
				$status: PlanStatus!,\s
				$visibility: PlanVisibility!,\s
				$trialDays: Int!
			  ) {
				updatePlan(
				  id: $id,
				  request: {
					name: $name,\s
					description: $description,
					priceMonthly: $priceMonthly,\s
					priceYearly: $priceYearly,\s
					maxUsers: $maxUsers,
					maxRoles: $maxRoles,\s
					maxAccounts: $maxAccounts,\s
					maxInvoices: $maxInvoices,
					status: $status,\s
					visibility: $visibility,\s
					trialDays: $trialDays
				  }
				)
			  }

		""";
		return updatePlanMutation;
	}
}
