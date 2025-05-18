package com.jaapec.tenant.plans.application.search;

import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;
import com.jaapec.tenant.shared.domain.criteria.Filters;
import com.jaapec.tenant.shared.domain.criteria.Order;
import com.jaapec.tenant.shared.domain.criteria.PaginatedResponse;

@Service
public final class SearchPlanQueryHandler implements QueryHandler<SearchPlanQuery, PaginatedResponse<PlanResponse>> {

	private final PlanSearcher searcher;

	public SearchPlanQueryHandler(PlanSearcher searcher) {
		this.searcher = searcher;
	}

	@Override
	public PaginatedResponse<PlanResponse> handle(SearchPlanQuery query) {
		Filters filters = Filters.fromValues(query.filters());
		Order order = Order.from(query.orderBy(), query.orderType());
		return searcher.search(filters, order, query.pagination());
	}
}
