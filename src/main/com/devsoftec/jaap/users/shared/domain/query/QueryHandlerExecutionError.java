package com.devsoftec.jaap.users.shared.domain.query;

public final class QueryHandlerExecutionError extends RuntimeException {

	public QueryHandlerExecutionError(Throwable cause) {
		super(cause);
	}
}
