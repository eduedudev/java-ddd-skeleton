package com.devsoftec.jaap.users.shared.domain.bus.command;

public final class CommandHandlerExecutionError extends RuntimeException {

	public CommandHandlerExecutionError(Throwable cause) {
		super(cause);
	}
}
