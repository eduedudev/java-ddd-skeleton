package com.jaapec.tenant.shared.infrastructure.controller.graphql;

import java.io.Serializable;
import java.util.List;

public class GraphQLExceptionList extends RuntimeException  implements Serializable {

	private final List<GraphQLCustomException> errors;

	public GraphQLExceptionList(List<GraphQLCustomException> errors) {
		this.errors = errors;
	}

	public GraphQLExceptionList() {
		this.errors = null;
	}

	public List<GraphQLCustomException> getErrors() {
		return errors;
	}
}
