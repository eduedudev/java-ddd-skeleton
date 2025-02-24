package com.devsoftec.jaap.users.shared.infrastructure.controller.graphql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;

import com.devsoftec.jaap.users.shared.domain.DomainError;
import com.devsoftec.jaap.users.shared.domain.Service;

@Service
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

	@Override
	protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
		if (ex instanceof DomainError) {
			return GraphqlErrorBuilder
				.newError()
				.errorType(ErrorType.BAD_REQUEST)
				.message(ex.getMessage())
				.path(env.getExecutionStepInfo().getPath())
				.location(env.getField().getSourceLocation())
				.extensions(
					new HashMap<>() {
						{
							put("reason", ((DomainError) ex).reason());
							put("code", ((DomainError) ex).errorCode());
						}
					}
				)
				.build();
		}

		return GraphqlErrorBuilder
			.newError()
			.errorType(ErrorType.BAD_REQUEST)
			.message(ex.getMessage())
			.path(env.getExecutionStepInfo().getPath())
			.location(env.getField().getSourceLocation())
			.build();
	}

	@Override
	protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
		if (ex instanceof GraphQLExceptionList) {
			List<GraphQLError> errors = new ArrayList<>();

			for (GraphQLCustomException error : ((GraphQLExceptionList) ex).getErrors()) {
				errors.add(
					GraphqlErrorBuilder
						.newError()
						.errorType(error.errorType())
						.message(error.message())
						.path(env.getExecutionStepInfo().getPath())
						.location(env.getField().getSourceLocation())
						.extensions(Collections.singletonMap("field", error.field()))
						.build()
				);
			}
			return errors;
		}
		GraphQLError error = resolveToSingleError(ex, env);
		return (error != null ? Collections.singletonList(error) : null);
	}
}
