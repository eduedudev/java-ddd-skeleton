package com.devsoftec.jaap.users.shared.domain.query;

public interface QueryBus {
	<R> R ask(Query query) throws QueryNotRegisteredError;
}
