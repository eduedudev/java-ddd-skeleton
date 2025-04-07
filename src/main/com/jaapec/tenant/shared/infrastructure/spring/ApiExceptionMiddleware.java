package com.jaapec.tenant.shared.infrastructure.spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.MessageTranslator;
import com.jaapec.tenant.shared.domain.Utils;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandlerExecutionError;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandlerExecutionError;

public final class ApiExceptionMiddleware implements Filter {

	private static final List<String> GRAPHQL_ENDPOINTS = List.of("/graphql", "/graphiql");
	private final RequestMappingHandlerMapping mapping;
	private final MessageTranslator translator;

	public ApiExceptionMiddleware(RequestMappingHandlerMapping mapping, MessageTranslator translator) {
		this.mapping = mapping;
		this.translator = translator;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);

		if (isGraphQLRequest(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			Object possibleController =
				((HandlerMethod) Objects.requireNonNull(mapping.getHandler(httpRequest)).getHandler()).getBean();

			try {
				chain.doFilter(request, response);
			} catch (Exception exception) {
				if (possibleController instanceof RestApiController) {
					handleCustomError(response, httpResponse, (RestApiController) possibleController, exception);
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void handleCustomError(
		ServletResponse response,
		HttpServletResponse httpResponse,
		RestApiController possibleController,
		Exception exception
	) throws IOException {
		Map<Class<? extends DomainError>, HttpStatus> errorMapping = possibleController.errorMapping();
		Throwable error = (
				exception.getCause() instanceof CommandHandlerExecutionError ||
				exception.getCause() instanceof QueryHandlerExecutionError
			)
			? exception.getCause().getCause()
			: exception.getCause();

		String errorMessage = error instanceof DomainError
			? translator.translate(error.getMessage(), ((DomainError) error).errorMessage().args())
			: translator.translate(error.getMessage(), new Object[] {});
		String errorReason = error instanceof DomainError ? ((DomainError) error).reason() : null;
		String errorValue = error instanceof DomainError ? ((DomainError) error).value() : null;

		int statusCode = statusFor(errorMapping, error);
		String errorCode = errorCodeFor(error);

		httpResponse.reset();
		httpResponse.setHeader("Content-Type", "application/json");
		httpResponse.setStatus(statusCode);
		PrintWriter writer = response.getWriter();

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> errorResponse = new LinkedHashMap<>(); // LinkedHashMap mantiene el orden
		errorResponse.put("error", true);
		errorResponse.put("code", errorCode);

		Map<String, Object> data = new LinkedHashMap<>();
		data.put("message", errorMessage);
		data.put("reason", errorReason);
		data.put("value", errorValue);
		errorResponse.put("data", data);
		writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse));

		writer.close();
	}

	private String errorCodeFor(Throwable error) {
		if (error instanceof DomainError) {
			return ((DomainError) error).errorCode();
		}

		return Utils.toSnake(error.getClass().toString());
	}

	private int statusFor(Map<Class<? extends DomainError>, HttpStatus> errorMapping, Throwable error) {
		return errorMapping.getOrDefault(error.getClass(), HttpStatus.INTERNAL_SERVER_ERROR).value();
	}

	private boolean isGraphQLRequest(HttpServletRequest request) {
		String path = request.getRequestURI().toLowerCase();
		return (
			GRAPHQL_ENDPOINTS.stream().anyMatch(path::contains) ||
			"application/graphql".equalsIgnoreCase(request.getContentType()) ||
			request.getParameter("query") != null
		);
	}
}
