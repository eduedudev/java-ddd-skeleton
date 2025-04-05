package com.jaapec.tenant.plans.infrastructure.controller.graphql.plan;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.application.find.FindPlanQuery;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;

@Controller
public final class PlanFinderDataFetcher extends GraphQLApiController {

	public PlanFinderDataFetcher(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	@QueryMapping("findPlan")
	public PlanResponse findPlan(@Argument String id) throws ResourceNotExist {
		return ask(new FindPlanQuery(id));
	}
}
