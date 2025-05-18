package com.jaapec.tenant.shared.infrastructure.controller.graphql;

import java.util.*;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.MessageTranslator;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.infrastructure.config.Parameter;
import com.jaapec.tenant.shared.infrastructure.config.ParameterNotExist;

@Service
public final class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

	private final Parameter config;
	private final MessageTranslator translator;

	public GraphQLExceptionResolver(Parameter config, MessageTranslator translator) {
		this.config = config;
		this.translator = translator;
	}

	@Override
	protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
		boolean debug = false;
		if (ex instanceof DomainError) {
			return customDomainError(ex, env);
		}
		Throwable newEx = unwrap(ex);

		if (newEx instanceof DomainError) {
			return customDomainError(newEx, env);
		}

		try {
			debug = Boolean.parseBoolean(config.get("DEBUG"));
		} catch (ParameterNotExist e) {
			System.out.println(e.getMessage());
		}

		if (debug) {
			return GraphqlErrorBuilder
				.newError()
				.errorType(ErrorType.INTERNAL_ERROR)
				.message(ex.getMessage())
				.path(env.getExecutionStepInfo().getPath())
				.location(env.getField().getSourceLocation())
				.build();
		}

		return GraphqlErrorBuilder
			.newError()
			.errorType(ErrorType.INTERNAL_ERROR)
			.message("Internal server error")
			.path(env.getExecutionStepInfo().getPath())
			.location(env.getField().getSourceLocation())
			.build();
	}

	@Override
	protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
		if (ex instanceof GraphQLExceptionList exceptionList) {
			return exceptionList
				.getErrors()
				.stream()
				.map(error ->
					GraphqlErrorBuilder
						.newError()
						.errorType(error.errorType())
						.message(error.message())
						.path(env.getExecutionStepInfo().getPath())
						.location(env.getField().getSourceLocation())
						.extensions(Collections.singletonMap("field", error.field()))
						.build()
				)
				.toList();
		}

		GraphQLError error = resolveToSingleError(ex, env);
		return error != null ? Collections.singletonList(error) : Collections.emptyList();
	}

	private Throwable unwrap(Throwable ex) {
		while (ex.getCause() != null && ex.getCause() != ex) {
			ex = ex.getCause();
		}
		return ex;
	}

	private GraphQLError customDomainError(Throwable ex, DataFetchingEnvironment env) {
		return GraphqlErrorBuilder
			.newError()
			.errorType(ErrorType.BAD_REQUEST)
			.message(
				translator.translate(((DomainError) ex).errorMessage().messageKey(), ((DomainError) ex).errorMessage().args())
			)
			.path(env.getExecutionStepInfo().getPath())
			.location(env.getField().getSourceLocation())
			.extensions(
				Map.of(
					"reason",
					((DomainError) ex).reason(),
					"value",
					((DomainError) ex).value(),
					"code",
					((DomainError) ex).errorCode()
				)
			)
			.build();
	}
}
