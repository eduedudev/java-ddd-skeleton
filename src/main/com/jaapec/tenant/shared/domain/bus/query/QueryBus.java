package com.jaapec.tenant.shared.domain.bus.query;

public interface QueryBus {
	<R> R ask(Query query) throws QueryHandlerExecutionError;
}
