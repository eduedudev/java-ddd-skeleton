package com.jaapec.tenant.shared.infrastructure.spring.log;

import java.io.Serializable;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import com.jaapec.tenant.shared.domain.Logger;
import com.jaapec.tenant.shared.domain.Service;

@Service
public final class Log4j2Logger implements Logger {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Log4j2Logger.class);

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void info(String message, Map<String, Serializable> context) {
		logger.info("{} Context: {}", message, context);
	}

	@Override
	public void warning(String message) {
		logger.warn(message);
	}

	@Override
	public void warning(String message, Map<String, Serializable> context) {
		logger.warn("{} Context: {}", message, context);
	}

	@Override
	public void critical(String message) {
		logger.fatal(message);
	}

	@Override
	public void critical(String message, Map<String, Serializable> context) {
		logger.fatal("{} Context: {}", message, context);
	}

	@Override
	public void error(String message) {
		logger.error(message);
	}

	@Override
	public void error(String message, Map<String, Serializable> context) {
		logger.error("{} Context: {}", message, context);
	}

	@Override
	public void error(String message, Map<String, Serializable> context, Throwable throwable) {
		logger.error("{} Context: {}", message, context, throwable);
	}

	@Override
	public void debug(String message) {
		logger.debug(message);
	}

	@Override
	public void debug(String message, Map<String, Serializable> context) {
		logger.debug("{} Context: {}", message, context);
	}

	@Override
	public void trace(String message, String traceId) {
		logger.trace("{} TraceId: {}", message, traceId);
	}

	@Override
	public void trace(String message, String traceId, Map<String, Serializable> context) {
		logger.trace("{} TraceId: {} Context: {}", message, traceId, context);
	}
}
