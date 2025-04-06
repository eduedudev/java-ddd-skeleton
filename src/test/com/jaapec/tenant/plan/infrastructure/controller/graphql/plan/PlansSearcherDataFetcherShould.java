package com.jaapec.tenant.plan.infrastructure.controller.graphql.plan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
public class PlansSearcherDataFetcherShould extends ApplicationTestCase {

	@Autowired
	private PlanRepository repository;

	@Test
	void return_plans_when_it_exists() throws Exception {
		for (int i = 0; i < 10; i++) {
			repository.save(PlanMother.random());
		}

		Map<String, Object> variables = Map.of(
			"filters",
			List.of(),
			"limit",
			10,
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		Integer totalItems = assertResponseWithBody(
			PlanGraphQLMother.searchPlansQuery(),
			"$.data.searchPlans.pagination.totalItems",
			variables,
			Integer.class
		);
		assertEquals(10, totalItems);
	}

	@Test
	void return_empty_list_when_it_does_not_exist() throws Exception {
		Map<String, Object> variables = Map.of(
			"filters",
			List.of(),
			"limit",
			10,
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		Integer totalItems = assertResponseWithBody(
			PlanGraphQLMother.searchPlansQuery(),
			"$.data.searchPlans.pagination.totalItems",
			variables,
			Integer.class
		);
		assertEquals(0, totalItems);
	}

	@Test
	void return_plans_with_status_active() throws Exception {
		for (int i = 0; i < 10; i++) {
			repository.save(PlanMother.createWithStatus("ACTIVE"));
		}
		for (int i = 0; i < 10; i++) {
			repository.save(PlanMother.createWithStatus("INACTIVE"));
		}

		Map<String, Object> variables = Map.of(
			"filters",
			List.of(Map.of("field", "status", "operator", "EQUAL", "value", "ACTIVE")),
			"limit",
			100,
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		Integer totalItems = assertResponseWithBody(
			PlanGraphQLMother.searchPlansQuery(),
			"$.data.searchPlans.pagination.totalItems",
			variables,
			Integer.class
		);
		assertEquals(10, totalItems);
	}

	@Test
	void return_plans_with_public_visibility_and_active_status() throws Exception {
		for (int i = 0; i < 5; i++) {
			repository.save(PlanMother.createWithVisibilityAndStatus("PUBLIC", "ACTIVE"));
		}
		for (int i = 0; i < 5; i++) {
			repository.save(PlanMother.createWithVisibilityAndStatus("PRIVATE", "ACTIVE"));
		}

		Map<String, Object> variables = Map.of(
			"filters",
			List.of(
				Map.of("field", "visibility", "operator", "EQUAL", "value", "PUBLIC"),
				Map.of("field", "status", "operator", "EQUAL", "value", "ACTIVE")
			),
			"limit",
			10,
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		Integer totalItems = assertResponseWithBody(
			PlanGraphQLMother.searchPlansQuery(),
			"$.data.searchPlans.pagination.totalItems",
			variables,
			Integer.class
		);
		assertEquals(5, totalItems);
	}

	@Test
	void return_paginated_results_based_on_limit_and_offset() throws Exception {
		for (int i = 0; i < 25; i++) {
			repository.save(PlanMother.random());
		}

		Map<String, Object> variables = Map.of(
			"filters",
			List.of(),
			"limit",
			10,
			"offset",
			10,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		List<?> resultData = assertResponseWithBody(
			PlanGraphQLMother.searchPlansQuery(),
			"$.data.searchPlans.data",
			variables,
			List.class
		);
		Integer totalItems = assertResponseWithBody(
			PlanGraphQLMother.searchPlansQuery(),
			"$.data.searchPlans.pagination.totalItems",
			variables,
			Integer.class
		);

		assertEquals(10, resultData.size());
		assertEquals(25, totalItems);
	}
}
