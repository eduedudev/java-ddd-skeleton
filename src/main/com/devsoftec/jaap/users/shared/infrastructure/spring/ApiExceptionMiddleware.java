package com.devsoftec.jaap.users.shared.infrastructure.spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Objects;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.devsoftec.jaap.users.shared.domain.DomainError;
import com.devsoftec.jaap.users.shared.domain.Utils;
import com.devsoftec.jaap.users.shared.domain.bus.command.CommandHandlerExecutionError;
import com.devsoftec.jaap.users.shared.domain.bus.query.QueryHandlerExecutionError;

public final class ApiExceptionMiddleware implements Filter {

	private final RequestMappingHandlerMapping mapping;

	public ApiExceptionMiddleware(RequestMappingHandlerMapping mapping) {
		this.mapping = mapping;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);

		try {
			Object possibleController =
				((HandlerMethod) Objects.requireNonNull(mapping.getHandler(httpRequest)).getHandler()).getBean();

			try {
				chain.doFilter(request, response);
			} catch (Exception exception) {
				if (possibleController instanceof ApiController) {
					handleCustomError(response, httpResponse, (ApiController) possibleController, exception);
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void handleCustomError(
		ServletResponse response,
		HttpServletResponse httpResponse,
		ApiController possibleController,
		Exception exception
	) throws IOException {
		HashMap<Class<? extends DomainError>, HttpStatus> errorMapping = possibleController.errorMapping();
		Throwable error = (
				exception.getCause() instanceof CommandHandlerExecutionError ||
				exception.getCause() instanceof QueryHandlerExecutionError
			)
			? exception.getCause().getCause()
			: exception.getCause();

		int statusCode = statusFor(errorMapping, error);
		String errorCode = errorCodeFor(error);
		String errorMessage = error.getMessage();

		httpResponse.reset();
		httpResponse.setHeader("Content-Type", "application/json");
		httpResponse.setStatus(statusCode);
		PrintWriter writer = response.getWriter();

		writer.write(
			String.format("{\"error\": true, \"code\": \"%s\", \"data\": {\"message\": \"%s\"}}", errorCode, errorMessage)
		);

		writer.close();
	}

	private String errorCodeFor(Throwable error) {
		if (error instanceof DomainError) {
			return ((DomainError) error).errorCode();
		}

		return Utils.toSnake(error.getClass().toString());
	}

	private int statusFor(HashMap<Class<? extends DomainError>, HttpStatus> errorMapping, Throwable error) {
		return errorMapping.getOrDefault(error.getClass(), HttpStatus.INTERNAL_SERVER_ERROR).value();
	}
}
