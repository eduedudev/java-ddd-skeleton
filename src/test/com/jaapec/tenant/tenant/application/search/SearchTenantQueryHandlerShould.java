package com.jaapec.tenant.tenant.application.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.shared.domain.criteria.*;
import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantMother;

final class SearchTenantQueryHandlerShould extends TenantModuleUnitTestCase {

	private SearchTenantQueryHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new SearchTenantQueryHandler(new TenantSearcher(repository));
	}

	@Test
	void should_search_all_tenants_when_no_criteria_provided() {
		// Given
		givenRandomTenants(5);

		SearchTenantQuery query = new SearchTenantQuery(List.of(), null, null, Pagination.defaults());

		// When
		PaginatedResponse<TenantResponse> response = handler.handle(query);

		// Then
		assertEquals(5, response.data().size());
	}

	@Test
	void should_filter_tenants_by_name_criteria() {
		// Given
		Tenant matchingTenant1 = TenantMother.withName("Acme Corporation");
		Tenant matchingTenant2 = TenantMother.withName("ACME LLC");
		Tenant nonMatchingTenant = TenantMother.withName("Other Company");

		List<Tenant> allTenants = List.of(matchingTenant1, matchingTenant2, nonMatchingTenant);

		// Simulamos que el repositorio devuelve solo los que coinciden con el filtro "acme"
		when(repository.matching(any()))
			.thenAnswer(invocation -> {
				Criteria criteria = invocation.getArgument(0);

				// Aquí simulamos la lógica del filtro (ej. contiene "acme", sin importar mayúsculas)
				return allTenants.stream().filter(t -> t.name().value().toLowerCase().contains("acme")).toList();
			});

		Filter filter = Filter.create("name", "CONTAINS", "acme");

		SearchTenantQuery query = new SearchTenantQuery(List.of(filter), null, null, Pagination.defaults());

		// When
		PaginatedResponse<TenantResponse> response = handler.handle(query);

		// Then
		assertEquals(2, response.data().size());
		List<String> expectedIds = List.of(matchingTenant1.id().value(), matchingTenant2.id().value());
		List<String> resultIds = response.data().stream().map(TenantResponse::id).toList();

		assertTrue(resultIds.containsAll(expectedIds));
	}

	@Test
	void should_order_tenants_by_specified_field_ascending() {
		// Given
		Tenant tenantA = TenantMother.withName("A Corp");
		Tenant tenantC = TenantMother.withName("C Corp");
		Tenant tenantB = TenantMother.withName("B Corp");
		givenTenants(List.of(tenantB, tenantC, tenantA));
		when(repository.matching(any()))
			.thenAnswer(invocation -> {
				return List.of(tenantA, tenantB, tenantC);
			});

		SearchTenantQuery query = new SearchTenantQuery(List.of(), "name", "asc", Pagination.defaults());

		// When
		PaginatedResponse<TenantResponse> response = handler.handle(query);

		// Then
		assertEquals(3, response.data().size());
		assertEquals(tenantA.id().value(), response.data().get(0).id());
		assertEquals(tenantB.id().value(), response.data().get(1).id());
		assertEquals(tenantC.id().value(), response.data().get(2).id());
	}

	private void givenTenants(List<Tenant> tenants) {
		tenants.forEach(tenant -> repository.save(tenant));
	}

	private void givenRandomTenants(int count) {
		List<Tenant> mockTenants = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			mockTenants.add(TenantMother.random());
		}

		when(repository.matching(argThat(criteria -> criteria.filters().filters().isEmpty()))).thenReturn(mockTenants);
	}
}
