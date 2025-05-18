package com.jaapec.tenant.tenant.application.search;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;
import com.jaapec.tenant.shared.domain.criteria.Filters;
import com.jaapec.tenant.shared.domain.criteria.Order;
import com.jaapec.tenant.shared.domain.criteria.PaginatedResponse;
import com.jaapec.tenant.tenant.application.TenantResponse;

@Service
public final class SearchTenantQueryHandler
	implements QueryHandler<SearchTenantQuery, PaginatedResponse<TenantResponse>> {

	private final TenantSearcher searcher;

	public SearchTenantQueryHandler(TenantSearcher searcher) {
		this.searcher = searcher;
	}

	@Override
	public PaginatedResponse<TenantResponse> handle(SearchTenantQuery query) {
		Filters filters = Filters.fromValues(query.filters());
		Order order = Order.from(query.orderBy(), query.orderType());
		return searcher.search(filters, order, query.pagination());
	}
}
