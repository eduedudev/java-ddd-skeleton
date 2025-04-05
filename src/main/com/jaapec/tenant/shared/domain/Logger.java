package com.jaapec.tenant.shared.domain;

import java.io.Serializable;
import java.util.Map;

public interface Logger {
	// general information
	void info(String message);

	void info(String message, Map<String, Serializable> context);

	// warnings
	void warning(String message);

	void warning(String message, Map<String, Serializable> context);

	// critical errors
	void critical(String message);

	void critical(String message, Map<String, Serializable> context);

	// general errors
	void error(String message);

	void error(String message, Map<String, Serializable> context);

	void error(String message, Map<String, Serializable> context, Throwable throwable);

	// Debugging
	void debug(String message);

	void debug(String message, Map<String, Serializable> context);

	// Traceability for distributed microservices
	void trace(String message, String traceId);

	void trace(String message, String traceId, Map<String, Serializable> context);
}
