package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.jaapec.tenant.shared.domain.UuidMother;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;
import com.jaapec.tenant.subscription.domain.*;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Transactional
class AddTenantSubscriptionShould extends ApplicationTestCase {

	@Autowired
	private TenantRepository tenantRepository;

	@Autowired
	private PlanRepository planRepository;

	private String addSubscriptionMutation() {
		return """
            mutation AddSubscription(
                $id: ID!,
                $request: RequestSubscription!
            ) {
                addSubscription(
                    id: $id,
                    request: $request
                )
            }
        """;
	}

	@Test
	void add_subscription_successfully_when_valid_data_is_provided() throws Exception {
		// Given a valid tenant
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);
		Plan plan = PlanMother.random();
		planRepository.save(plan);

		// And a valid subscription request
		Map<String, Object> request = new HashMap<>();
		request.put("planId", plan.id().value());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// When adding the subscription
		Boolean result = assertResponseWithBody(
			addSubscriptionMutation(),
			"$.data.addSubscription",
			variables,
			Boolean.class
		);

		// Then it should be successful
		assertTrue(result);
	}

	@Test
	void fail_when_plan_does_not_exist() throws Exception {
		// Given a non-existent tenant ID
		String nonExistentId = UUID.randomUUID().toString();

		// And a valid subscription request
		Map<String, Object> request = new HashMap<>();
		request.put("planId", UUID.randomUUID().toString());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", nonExistentId);
		variables.put("request", request);

		// When adding the subscription to a non-existent tenant
		// Then it should fail with appropriate error
		assertErrorResponse(addSubscriptionMutation(), variables, "The plan doesnt exist", Map.of());
	}

	@Test
	void fail_when_tenant_does_not_exist() throws Exception {
		// Given a non-existent tenant ID
		String nonExistentId = UUID.randomUUID().toString();

		Plan plan = PlanMother.random();
		planRepository.save(plan);

		// And a valid subscription request
		Map<String, Object> request = new HashMap<>();
		request.put("planId", plan.id().value());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", nonExistentId);
		variables.put("request", request);

		// When adding the subscription to a non-existent tenant
		// Then it should fail with appropriate error
		assertErrorResponse(addSubscriptionMutation(), variables, "The tenant doesnt exist", Map.of());
	}

	@Test
	void fail_when_plan_id_is_invalid() throws Exception {
		// Given a valid tenant
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		// And a subscription request with invalid plan ID
		Map<String, Object> request = new HashMap<>();
		request.put("planId", "invalid-uuid");
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// When adding the subscription with invalid plan ID
		// Then it should fail with appropriate error
		assertErrorResponse(addSubscriptionMutation(), variables, "The field planId is not a valid uuid", Map.of());
	}

	@Test
	void fail_when_billing_interval_is_invalid() throws Exception {
		// Given a valid tenant
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		// And a subscription request with invalid billing interval
		Map<String, Object> request = new HashMap<>();
		request.put("planId", UUID.randomUUID().toString());
		request.put("billingInterval", "INVALID");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// When adding the subscription with invalid billing interval
		// Then it should fail with appropriate error
		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"Variable 'request' has an invalid value: Invalid input for enum 'BillingInterval'. No value found for name 'INVALID'",
			Map.of()
		);
	}

	@Test
	void fail_when_pricing_is_negative() throws Exception {
		// Given a valid tenant
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		Plan plan = PlanMother.random();
		planRepository.save(plan);

		// And a subscription request with negative pricing
		Map<String, Object> request = new HashMap<>();
		request.put("planId", plan.id().value());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", -100);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// When adding the subscription with negative pricing
		// Then it should fail with appropriate error
		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"Field pricing must be less than or equal to 0",
			Map.of()
		);
	}

	@Test
	void fail_when_currency_is_invalid() throws Exception {
		// Given a valid tenant
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		// And a subscription request with invalid currency
		Map<String, Object> request = new HashMap<>();
		request.put("planId", UUID.randomUUID().toString());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "EUR");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// When adding the subscription with invalid currency
		// Then it should fail with appropriate error
		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"Variable 'request' has an invalid value: Invalid input for enum 'Currency'. No value found for name 'EUR'",
			Map.of()
		);
	}

	@Test
	void fail_when_auto_renew_is_missing() throws Exception {
		// Given a valid tenant
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		// And a subscription request with missing autoRenew
		Map<String, Object> request = new HashMap<>();
		request.put("planId", UUID.randomUUID().toString());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		// autoRenew is missing

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// When adding the subscription with missing autoRenew
		// Then it should fail with appropriate error
		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"Variable 'request' has an invalid value: Field 'autoRenew' has coerced Null value for NonNull type 'Boolean!'",
			Map.of()
		);
	}

	@Test
	void add_subscription_with_yearly_billing_interval() throws Exception {
		// Given a valid tenant
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		Plan plan = PlanMother.random();
		planRepository.save(plan);

		// And a valid subscription request with YEARLY billing interval
		Map<String, Object> request = new HashMap<>();
		request.put("planId", plan.id().value());
		request.put("billingInterval", "YEARLY");
		request.put("pricing", 10000);
		request.put("currency", "USD");
		request.put("coupon", "ANNUAL20");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// When adding the subscription
		Boolean result = assertResponseWithBody(
			addSubscriptionMutation(),
			"$.data.addSubscription",
			variables,
			Boolean.class
		);

		// Then it should be successful
		assertTrue(result);
	}

	@Test
	void fail_when_plan_id_is_missing() throws Exception {
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		Map<String, Object> request = new HashMap<>();
		// planId is missing
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"Variable 'request' has an invalid value: Field 'planId' has coerced Null value for NonNull type 'String!'",
			Map.of()
		);
	}

	@Test
	void fail_when_pricing_is_string() throws Exception {
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		Plan plan = PlanMother.random();
		planRepository.save(plan);

		Map<String, Object> request = new HashMap<>();
		request.put("planId", plan.id().value());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", "one thousand"); // incorrect type
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"Variable 'request' has an invalid value: Expected a value that can be converted to type 'Int' but it was a 'String'",
			Map.of()
		);
	}

	@Test
	void fail_when_active_subscription_already_exists() throws Exception {
		// Given a tenant with an active subscription
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);
		Plan plan = PlanMother.random();
		planRepository.save(plan);
		// Create a subscription with ACTIVE status
		SubscriptionId subscriptionId = new SubscriptionId(UuidMother.generate());
		Tenant tenantWithSubscription = tenant.subscribeToPlan(
			subscriptionId,
			plan,
			new BillingInterval(BillingInterval.intervals.MONTHLY.toString()),
			new SubscriptionPricing(2596),
			new Currency(Currency.currency.USD.toString()),
			null,
			new SubscriptionSource(SubscriptionSource.source.BACKOFFICE.toString()),
			new SubscriptionAutoRenew(false)
		);
		tenantRepository.update(tenantWithSubscription);

		Tenant tenantWithSubscriptionActive = tenantWithSubscription.activateSubscription(
			subscriptionId,
			new SubscriptionPaymentMethod("test"),
			new SubscriptionPaymentReference("12312")
		);

		tenantRepository.update(tenantWithSubscriptionActive);
		// When trying to add another subscription with the same plan
		Map<String, Object> request = new HashMap<>();
		request.put("planId", plan.id().value());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// Then it should fail with ActiveSubscriptionAlreadyExistsException
		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"There is already an active subscription for this tenant",
			Map.of("code", "E424")
		);
	}

	@Test
	void fail_when_pending_subscription_exists() throws Exception {
		// Given a tenant with a pending subscription
		Tenant tenant = TenantMother.random();
		tenantRepository.save(tenant);

		Plan plan = PlanMother.random();
		planRepository.save(plan);

		// Create a subscription with PENDING payment status
		SubscriptionId subscriptionId = new SubscriptionId(UuidMother.generate());
		Tenant tenantWithSubscription = tenant.subscribeToPlan(
			subscriptionId,
			plan,
			new BillingInterval(BillingInterval.intervals.MONTHLY.toString()),
			new SubscriptionPricing(2596),
			new Currency(Currency.currency.USD.toString()),
			null,
			new SubscriptionSource(SubscriptionSource.source.BACKOFFICE.toString()),
			new SubscriptionAutoRenew(false)
		);
		tenantRepository.update(tenantWithSubscription);

		// When trying to add another subscription
		Plan anotherPlan = PlanMother.random();
		planRepository.save(anotherPlan);

		Map<String, Object> request = new HashMap<>();
		request.put("planId", anotherPlan.id().value());
		request.put("billingInterval", "MONTHLY");
		request.put("pricing", 1000);
		request.put("currency", "USD");
		request.put("autoRenew", true);

		Map<String, Object> variables = new HashMap<>();
		variables.put("id", tenant.id().value());
		variables.put("request", request);

		// Then it should fail with PendingSubscriptionExistsException
		assertErrorResponse(
			addSubscriptionMutation(),
			variables,
			"There is already a subscription pending payment",
			Map.of("code", "E425")
		);
	}
}
