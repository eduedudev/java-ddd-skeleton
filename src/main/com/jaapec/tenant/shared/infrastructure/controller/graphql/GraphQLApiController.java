package com.jaapec.tenant.shared.infrastructure.controller.graphql;

import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.spring.ApiController;

public abstract class GraphQLApiController extends ApiController {

	protected GraphQLApiController(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}
}
