package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;
import com.jaapec.tenant.tenant.domain.*;

@Transactional
class TenantsSearcherShould extends ApplicationTestCase {

	@Autowired
	private TenantRepository repository;

	@Test
	void should_return_all_tenants_when_no_filters_are_applied() throws Exception {
		for (int i = 0; i < 10; i++) {
			repository.save(TenantMother.random());
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
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.pagination.totalItems",
			variables,
			Integer.class
		);
		assertEquals(10, totalItems);
	}

	@Test
	void should_return_empty_list_when_no_tenants_exist() throws Exception {
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
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.pagination.totalItems",
			variables,
			Integer.class
		);
		assertEquals(0, totalItems);
	}

	@Test
	void should_return_paginated_results_according_to_limit_and_offset() throws Exception {
		for (int i = 0; i < 25; i++) {
			repository.save(TenantMother.random());
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
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.data",
			variables,
			List.class
		);
		Integer totalItems = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.pagination.totalItems",
			variables,
			Integer.class
		);

		assertEquals(10, resultData.size());
		assertEquals(25, totalItems);
	}

	@Test
	void should_return_tenants_matching_name_criteria() throws Exception {
		Tenant specificTenant = TenantMother.withName("Acme Corporation");
		repository.save(specificTenant);
		for (int i = 0; i < 5; i++) {
			repository.save(TenantMother.random());
		}

		Map<String, Object> variables = Map.of(
			"filters",
			List.of(Map.of("field", "name", "operator", "CONTAINS", "value", "Acme")),
			"limit",
			10,
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		List<?> resultData = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.data",
			variables,
			List.class
		);
		Integer totalItems = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.pagination.totalItems",
			variables,
			Integer.class
		);

		assertEquals(1, totalItems);
		assertEquals("Acme Corporation", ((Map<?, ?>) resultData.getFirst()).get("name"));
	}

	@Test
	void should_return_tenants_matching_multiple_criteria() throws Exception {
		// Given: Inquilinos con diferentes atributos
		TenantOwnerId ownerId1 = TenantOwnerIdMother.random();
		TenantOwnerId ownerId2 = TenantOwnerIdMother.random();

		repository.save(TenantMother.withNameAndOwnerId("DevOps Inc", ownerId1.value()));
		repository.save(TenantMother.withNameAndOwnerId("DevOps LLC", ownerId2.value()));
		repository.save(TenantMother.withNameAndOwnerId("Tech Corp", ownerId2.value()));

		// When: Aplicamos múltiples filtros combinados
		Map<String, Object> variables = Map.of(
			"filters",
			List.of(
				Map.of("field", "name", "operator", "CONTAINS", "value", "DevOps"),
				Map.of("field", "ownerId", "operator", "EQUAL", "value", ownerId2.value())
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

		// Then: Solo devuelve inquilinos que cumplen todos los criterios
		Integer totalItems = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.pagination.totalItems",
			variables,
			Integer.class
		);

		assertEquals(1, totalItems);
	}

	@Test
	void should_return_tenants_sorted_by_specified_field_and_direction() throws Exception {
		// Given: Varios inquilinos con nombres distintos
		repository.save(TenantMother.withName("Zebra Tech"));
		repository.save(TenantMother.withName("Acme Corp"));
		repository.save(TenantMother.withName("Middle Systems"));

		// When: Ordenamos por nombre descendente
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
			"DESC"
		);

		// Then: Los resultados aparecen en el orden correcto
		List<Map<String, Object>> resultData = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.data",
			variables,
			List.class
		);

		assertEquals(3, resultData.size());
		assertEquals("Zebra Tech", ((Map<?, ?>) resultData.get(0)).get("name"));
		assertEquals("Middle Systems", ((Map<?, ?>) resultData.get(1)).get("name"));
		assertEquals("Acme Corp", ((Map<?, ?>) resultData.get(2)).get("name"));
	}

	@Test
	void should_respect_maximum_limit_constraint() throws Exception {
		// Given: Más inquilinos que el límite máximo permitido
		int maxAllowedLimit = 100; // Ajusta según tu implementación
		for (int i = 0; i < 105; i++) {
			repository.save(TenantMother.random());
		}

		// When: Solicitamos más del límite máximo
		Map<String, Object> variables = Map.of(
			"filters",
			List.of(),
			"limit",
			105, // Intentamos pedir muchos más
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		// Then: Solo devuelve hasta el máximo permitido
		List<?> resultData = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.data",
			variables,
			List.class
		);

		assertTrue(resultData.size() <= maxAllowedLimit);
	}

	@Test
	void should_perform_case_insensitive_search() throws Exception {
		// Given: Inquilinos con nombres con diferentes capitalizaciones
		repository.save(TenantMother.withName("UPPERCASE CORP"));
		repository.save(TenantMother.withName("lowercase systems"));

		// When: Buscamos ignorando caso
		Map<String, Object> variables = Map.of(
			"filters",
			List.of(Map.of("field", "name", "operator", "CONTAINS", "value", "corp")),
			"limit",
			10,
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		// Then: Encontramos coincidencias independientes del caso
		Integer totalItems = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.pagination.totalItems",
			variables,
			Integer.class
		);

		assertEquals(1, totalItems);
	}

	@Test
	void should_find_tenants_with_partial_text_match() throws Exception {
		// Given: Inquilinos con nombres similares pero no idénticos
		repository.save(TenantMother.withName("Tech Solutions Inc"));
		repository.save(TenantMother.withName("Advanced Tech"));
		repository.save(TenantMother.withName("Non-matching Corp"));

		// When: Buscamos por texto parcial
		Map<String, Object> variables = Map.of(
			"filters",
			List.of(Map.of("field", "name", "operator", "CONTAINS", "value", "Tech")),
			"limit",
			10,
			"offset",
			0,
			"orderBy",
			"name",
			"orderType",
			"ASC"
		);

		// Then: Encontramos todos los que contienen el texto parcial
		Integer totalItems = assertResponseWithBody(
			TenantGraphQLMother.searchTenantsQuery(),
			"$.data.searchTenants.pagination.totalItems",
			variables,
			Integer.class
		);

		assertEquals(2, totalItems);
	}
}
