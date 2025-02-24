package com.devsoftec.jaap.users.shared.infrastructure;

import java.io.Serializable;
import java.util.Map;

import com.devsoftec.jaap.users.shared.domain.Logger;

public class Log4jLogger implements Logger {

	@Override
	public void info(String $message) {}

	@Override
	public void info(String $message, Map<String, Serializable> $context) {}

	@Override
	public void warning(String $message) {}

	@Override
	public void warning(String $message, Map<String, Serializable> $context) {}

	@Override
	public void critical(String $message) {}

	@Override
	public void critical(String $message, Map<String, Serializable> $context) {}
}
