package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.BillingInterval;
import com.jaapec.tenant.plans.domain.value_objects.Currency;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.application.change_domain.ChangeTenantDomainCommand;
import com.jaapec.tenant.tenant.application.create.CreateTenantCommand;
import com.jaapec.tenant.tenant.application.update.UpdateTenantCommand;
import com.jaapec.tenant.tenant.domain.*;

@Transactional
class TenantMutationsDataFetcherShould extends ApplicationTestCase {

	@Autowired
	private TenantRepository repository;

	@Autowired
	private PlanRepository planRepository;

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

	@Test
	void should_fail_when_updating_tenant_with_invalid_name() throws Exception {
		Tenant tenant = TenantMother.random();
		repository.save(tenant);
		UpdateTenantCommand command = new UpdateTenantCommand(tenant.id().value(), "");
		Map<String, Object> variables = new HashMap<>();
		variables.put("id", command.id());
		variables.put("name", command.name());
		assertErrorResponse(
			TenantGraphQLMother.updateTenantMutation(),
			variables,
			"The field name should not be empty",
			Map.of("field", "name")
		);
	}

	@Test
	void should_store_custom_domain_in_tenant_when_valid_domain_provided() throws Exception {
		Tenant tenant = TenantMother.random();
		repository.save(tenant);
		ChangeTenantDomainCommand command = new ChangeTenantDomainCommand(tenant.id().value(), "dash.example.com");

		Map<String, Object> request = new HashMap<>();
		request.put("domain", command.domain());

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", command.id());
		variables.put("request", request);
		assertResponse(TenantGraphQLMother.changeDomainMutation(), "$.data.changeDomain", variables);

		Map<String, Object> queryVariables = Map.of("id", tenant.id().value());
		TenantResponse tenantResponse = assertResponseWithBody(
			TenantGraphQLMother.findTenantQuery(),
			"$.data.findTenant",
			queryVariables,
			TenantResponse.class
		);

		assertEquals("dash.example.com", tenantResponse.domain());
	}

	@Test
	void should_reject_custom_domain_when_already_exists_for_another_tenant() throws Exception {
		Tenant tenant1 = TenantMother.random();
		repository.save(tenant1);
		Tenant tenantAddDomain = TenantMother.randomWithDomain(tenant1, "dash.example.com");
		repository.update(tenantAddDomain);

		Tenant tenant = TenantMother.random();
		repository.save(tenant);

		ChangeTenantDomainCommand command = new ChangeTenantDomainCommand(tenant.id().value(), "dash.example.com");
		Map<String, Object> request = new HashMap<>();
		request.put("domain", command.domain());

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", command.id());
		variables.put("request", request);

		assertErrorResponse(
			TenantGraphQLMother.changeDomainMutation(),
			variables,
			String.format("The domain %s already exists", command.domain()),
			Map.of("code", "E410", "reason", "domain", "value", command.domain())
		);
	}

	@Test
	void should_cancel_auto_renew_for_subscription() throws Exception {
		// Given a tenant
		Tenant tenant = TenantMother.random();
		repository.save(tenant);

		// And a plan
		Plan plan = PlanMother.random();
		planRepository.save(plan);

		// And a subscription with auto-renew enabled
		SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
		Tenant tenantWithSubscription = tenant.subscribeToPlan(
			subscriptionId,
			plan,
			new BillingInterval(BillingInterval.intervals.MONTHLY.toString()),
			new SubscriptionPricing(2596),
			new Currency(Currency.currency.USD.toString()),
			null,
			new SubscriptionSource(SubscriptionSource.source.BACKOFFICE.toString()),
			new SubscriptionAutoRenew(true)
		);
		repository.update(tenantWithSubscription);

		// And an activated subscription
		Tenant tenantWithActiveSubscription = tenantWithSubscription.activateSubscription(
			subscriptionId,
			new SubscriptionPaymentMethod("test"),
			new SubscriptionPaymentReference("12312")
		);
		repository.update(tenantWithActiveSubscription);

		// When canceling auto-renew
		Map<String, Object> variables = new HashMap<>();
		variables.put("tenantId", tenant.id().value());
		variables.put("subscriptionId", subscriptionId.value());

		// Then it should be successful
		assertResponse(TenantGraphQLMother.cancelAutoRenewMutation(), "$.data.cancelAutoRenew", variables);
	}
}
