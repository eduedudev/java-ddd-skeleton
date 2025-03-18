package com.jaapec.tenant.shared.domain.bus.command;

public final class CommandHandlerExecutionError extends RuntimeException {

	public CommandHandlerExecutionError(Throwable cause) {
		super(cause);
	}
}
