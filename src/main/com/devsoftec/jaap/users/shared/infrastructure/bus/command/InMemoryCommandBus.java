package com.devsoftec.jaap.users.shared.infrastructure.bus.command;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.bus.command.Command;
import com.devsoftec.jaap.users.shared.domain.bus.command.CommandBus;

import com.devsoftec.jaap.users.shared.domain.bus.command.CommandHandler;
import com.devsoftec.jaap.users.shared.domain.bus.command.CommandNotRegisteredError;
import org.springframework.context.ApplicationContext;


@Service
public final class InMemoryCommandBus implements CommandBus {
    private final CommandHandlersInformation information;
    private final ApplicationContext context;

    public InMemoryCommandBus(CommandHandlersInformation information, ApplicationContext context) {
        this.information = information;
        this.context     = context;
    }

    @Override
    public void dispatch(Command command) throws CommandNotRegisteredError {
        Class<? extends CommandHandler> commandHandlerClass = information.search(command.getClass());

        CommandHandler handler = context.getBean(commandHandlerClass);

        handler.handle(command);
    }
}
