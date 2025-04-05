package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import java.util.HashMap;
import java.util.Map;

import org.intellij.lang.annotations.Language;

import com.jaapec.tenant.plans.application.create.CreatePlanCommand;

public final class PlanGraphQLMother {

	public static String createPlanMutation() {
		@Language("GraphQL")
		String mutation =
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
		return mutation;
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
}
