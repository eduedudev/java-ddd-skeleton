package com.jaapec.tenant.plan.application.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.plan.PlanModuleUnitTestCase;
import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.application.search.PlanSearcher;
import com.jaapec.tenant.plans.application.search.SearchPlanQuery;
import com.jaapec.tenant.plans.application.search.SearchPlanQueryHandler;
import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.value_objects.PlanStatus;
import com.jaapec.tenant.shared.domain.criteria.*;

final class SearchPlanQueryHandlerShould extends PlanModuleUnitTestCase {

	private SearchPlanQueryHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new SearchPlanQueryHandler(new PlanSearcher(repository));
	}

	@Test
	void should_filter_by_status_active() {
		Filter filter = new Filter(new FilterField("status"), FilterOperator.EQUAL, new FilterValue("ACTIVE"));
		SearchPlanQuery query = new SearchPlanQuery(List.of(filter), null, null, Pagination.defaults());
		when(repository.matching(any())).thenReturn(List.of());
		handler.handle(query);
		verify(repository)
			.matching(
				argThat(criteria ->
					criteria.filters().filters().getFirst().field().value().equals("status") &&
					criteria.filters().filters().getFirst().value().value().equals("ACTIVE")
				)
			);
	}

	@Test
	void should_apply_pagination() {
		Pagination pagination = new Pagination(10, 20);
		SearchPlanQuery query = new SearchPlanQuery(List.of(), null, null, pagination);
		when(repository.matching(any())).thenReturn(List.of());
		handler.handle(query);
		verify(repository)
			.matching(
				argThat(criteria -> criteria.pagination().limit() == 10 && criteria.pagination().offset() == 19 // Adjusted for zero-based index
				)
			);
	}

	@Test
	void should_order_by_name_desc() {
		SearchPlanQuery query = new SearchPlanQuery(List.of(), "name", "desc", Pagination.defaults());
		when(repository.matching(any())).thenReturn(List.of());
		handler.handle(query);
		verify(repository)
			.matching(argThat(criteria -> criteria.order().field().equals("name") && criteria.order().direction().isDesc()));
	}

	@Test
	void should_return_all_plans_ignoring_filter() {
		List<Plan> mockPlans = Stream
			.concat(
				IntStream.range(0, 10).mapToObj(i -> PlanMother.createWithStatus(PlanStatus.status.ACTIVE.toString())),
				IntStream.range(0, 5).mapToObj(i -> PlanMother.createWithStatus(PlanStatus.status.INACTIVE.toString()))
			)
			.toList();

		when(repository.matching(any()))
			.thenAnswer(invocation -> {
				Criteria criteria = invocation.getArgument(0);
				Pagination pagination = criteria.pagination();

				return mockPlans.stream().skip(pagination.offset()).limit(pagination.limit()).toList();
			});
		when(repository.count(any()))
			.thenAnswer(invocation ->
				mockPlans.stream().filter(plan -> PlanStatus.status.ACTIVE.toString().equals(plan.status().value())).count()
			);
		Filter filter = new Filter(new FilterField("status"), FilterOperator.EQUAL, new FilterValue("INACTIVE"));
		SearchPlanQuery query = new SearchPlanQuery(List.of(filter), "name", "desc", Pagination.fromValues(100, 0));

		PaginatedResponse<PlanResponse> results = handler.handle(query);

		verify(repository).matching(any());

		assertEquals(10, results.pagination().totalItems());
	}
}
