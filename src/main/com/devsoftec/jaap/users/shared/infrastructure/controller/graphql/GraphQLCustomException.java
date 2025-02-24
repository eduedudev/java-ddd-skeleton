package com.devsoftec.jaap.users.shared.infrastructure.controller.graphql;

import org.springframework.graphql.execution.ErrorType;

public class GraphQLCustomException {

	private final String message;
	private final ErrorType errorType;

	private final String field;

	public GraphQLCustomException(String message, String field, String errorType) {
		this.message = message;
		this.errorType = ErrorType.valueOf(errorType);
		this.field = field;
	}

	public GraphQLCustomException(String message, String field) {
		this.message = message;
		this.errorType = ErrorType.BAD_REQUEST;
		this.field = field;
	}

	public String message() {
		return message;
	}

	public ErrorType errorType() {
		return errorType;
	}

	public String field() {
		return field;
	}
}
