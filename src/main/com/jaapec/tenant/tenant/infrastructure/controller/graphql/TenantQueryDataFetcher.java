package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import java.util.List;
import javax.annotation.Nullable;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.domain.criteria.Filter;
import com.jaapec.tenant.shared.domain.criteria.PaginatedResponse;
import com.jaapec.tenant.shared.domain.criteria.Pagination;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.application.search.SearchTenantQuery;

@Controller
public final class TenantQueryDataFetcher extends GraphQLApiController {

	public TenantQueryDataFetcher(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	@QueryMapping("searchTenants")
	public PaginatedResponse<TenantResponse> searchTenants(
		@Argument List<Filter> filters,
		@Argument String orderBy,
		@Argument String orderType,
		@Argument @Nullable Integer limit,
		@Argument @Nullable Integer offset
	) {
		Pagination pagination = (limit == null || offset == null)
			? Pagination.defaults()
			: Pagination.fromValues(limit, offset);
		return ask(new SearchTenantQuery(filters != null ? filters : List.of(), orderBy, orderType, pagination));
	}
}
