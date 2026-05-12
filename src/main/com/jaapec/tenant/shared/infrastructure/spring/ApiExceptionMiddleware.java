package com.jaapec.tenant.shared.infrastructure.spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (isGraphQLRequest(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}

		Object possibleController = getHandlerController(httpRequest);

		if (possibleController == null) {
			httpResponse.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
			return;
		}

		try {
			chain.doFilter(request, response);
		} catch (Exception exception) {
			if (possibleController instanceof RestApiController controller) {
				handleCustomError(response, httpResponse, controller, exception);
			}
		}
	}

	private Object getHandlerController(HttpServletRequest request) {
		try {
			var handler = mapping.getHandler(request);
			if (handler == null) {
				return null;
			}
			HandlerMethod handlerMethod = (HandlerMethod) handler.getHandler();
			return handlerMethod.getBean();
		} catch (Exception e) {
			return null;
		}
	}

	private void handleCustomError(
		ServletResponse response,
		HttpServletResponse httpResponse,
		RestApiController possibleController,
		Exception exception
	) throws IOException {
		Map<Class<? extends DomainError>, HttpStatus> errorMapping = possibleController.errorMapping();
		Throwable rootError = extractRootError(exception);

		String errorMessage = extractErrorMessage(rootError);
		Map<String, String> errorDetails = extractErrorDetails(rootError);

		int statusCode = statusFor(errorMapping, rootError);
		String errorCode = errorCodeFor(rootError);

		writeErrorResponse(response, httpResponse, statusCode, errorCode, errorMessage, errorDetails);
	}

	private Throwable extractRootError(Exception exception) {
		Throwable rootCause = exception.getCause();
		if (rootCause instanceof CommandHandlerExecutionError || rootCause instanceof QueryHandlerExecutionError) {
			return rootCause.getCause();
		}
		return rootCause != null ? rootCause : exception;
	}

	private String extractErrorMessage(Throwable error) {
		if (error instanceof DomainError domainError) {
			return translator.translate(domainError.errorMessage().messageKey(), domainError.errorMessage().args());
		}
		return translator.translate(error.getMessage(), new Object[] {});
	}

	private Map<String, String> extractErrorDetails(Throwable error) {
		Map<String, String> details = new LinkedHashMap<>();
		if (error instanceof DomainError domainError) {
			details.put("reason", domainError.reason());
			details.put("value", domainError.value());
		} else {
			details.put("reason", null);
			details.put("value", null);
		}
		return details;
	}

	private void writeErrorResponse(
		ServletResponse response,
		HttpServletResponse httpResponse,
		int statusCode,
		String errorCode,
		String errorMessage,
		Map<String, String> errorDetails
	) throws IOException {
		httpResponse.reset();
		httpResponse.setHeader("Content-Type", "application/json");
		httpResponse.setStatus(statusCode);

		Map<String, Object> errorResponse = new LinkedHashMap<>();
		errorResponse.put("error", true);
		errorResponse.put("code", errorCode);

		Map<String, Object> data = new LinkedHashMap<>();
		data.put("message", errorMessage);
		data.put("reason", errorDetails.get("reason"));
		data.put("value", errorDetails.get("value"));
		errorResponse.put("data", data);

		try (PrintWriter writer = response.getWriter()) {
			Jsonb jsonb = JsonbBuilder.create();
			String jsonResponse = jsonb.toJson(errorResponse);
			writer.write(jsonResponse);
		}
	}

	private String errorCodeFor(Throwable error) {
		if (error instanceof DomainError domainError) {
			return domainError.errorCode();
		}

		return Utils.toSnake(error.getClass().getSimpleName());
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
