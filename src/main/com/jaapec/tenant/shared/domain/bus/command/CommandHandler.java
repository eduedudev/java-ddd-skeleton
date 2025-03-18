package com.jaapec.tenant.shared.domain.bus.command;

public interface CommandHandler<T extends Command> {
	void handle(T command);
}
