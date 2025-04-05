package com.jaapec.tenant.plans.infrastructure.controller.graphql.plan;

import java.util.List;
import javax.annotation.Nullable;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.application.search.SearchPlanQuery;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.domain.criteria.Filter;
import com.jaapec.tenant.shared.domain.criteria.PaginatedResponse;
import com.jaapec.tenant.shared.domain.criteria.Pagination;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;

@Controller
public final class PlanSearcherDataFetcher extends GraphQLApiController {

	public PlanSearcherDataFetcher(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	@QueryMapping("searchPlans")
	public PaginatedResponse<PlanResponse> searchPlan(
		@Argument List<Filter> filters,
		@Argument String orderBy,
		@Argument String orderType,
		@Argument @Nullable Integer limit,
		@Argument @Nullable Integer offset
	) throws ResourceNotExist {
		Pagination pagination = (limit == null || offset == null)
			? Pagination.defaults()
			: Pagination.fromValues(limit, offset);
		return ask(new SearchPlanQuery(filters != null ? filters : List.of(), orderBy, orderType, pagination));
	}
}
