package com.devsoftec.jaap.users.shared.infrastructure.bus.query;

import org.springframework.context.ApplicationContext;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.query.*;

@Service
public final class InMemoryQueryBus implements QueryBus {

	private final QueryHandlersInformation information;
	private final ApplicationContext context;

	public InMemoryQueryBus(QueryHandlersInformation information, ApplicationContext context) {
		this.information = information;
		this.context = context;
	}

	@Override
	public Response ask(Query query) throws QueryNotRegisteredError {
		Class<? extends QueryHandler> queryHandlerClass = information.search(query.getClass());

		QueryHandler handler = context.getBean(queryHandlerClass);

		return handler.handle(query);
	}
}
