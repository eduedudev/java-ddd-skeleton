package com.jaapec.tenant.shared.infrastructure.spring;

import com.jaapec.tenant.shared.domain.bus.command.Command;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandlerExecutionError;
import com.jaapec.tenant.shared.domain.bus.query.Query;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandlerExecutionError;

public abstract class ApiController {

	private final QueryBus queryBus;
	private final CommandBus commandBus;

	protected ApiController(QueryBus queryBus, CommandBus commandBus) {
		this.queryBus = queryBus;
		this.commandBus = commandBus;
	}

	protected void dispatch(Command command) throws CommandHandlerExecutionError {
		commandBus.dispatch(command);
	}

	protected <R> R ask(Query query) throws QueryHandlerExecutionError {
		return queryBus.ask(query);
	}
}
