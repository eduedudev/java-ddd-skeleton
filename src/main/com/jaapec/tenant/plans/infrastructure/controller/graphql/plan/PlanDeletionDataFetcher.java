package com.jaapec.tenant.plans.infrastructure.controller.graphql.plan;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.plans.application.delete.DeletePlanCommand;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;

@Controller
public final class PlanDeletionDataFetcher extends GraphQLApiController {

	public PlanDeletionDataFetcher(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	@MutationMapping
	public boolean deletePlan(@Argument String id) {
		dispatch(new DeletePlanCommand(id));
		return true;
	}
}
