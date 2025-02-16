package com.devsoftec.jaap.users.shared.infrastructure.bus.command;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.bus.command.Command;
import com.devsoftec.jaap.users.shared.domain.bus.command.CommandHandler;

import com.devsoftec.jaap.users.shared.domain.bus.command.CommandNotRegisteredError;
import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public final class CommandHandlersInformation {
    Map<Class<? extends Command>, Class<? extends CommandHandler>> indexedCommandHandlers;

    public CommandHandlersInformation() {
        Reflections reflections = new Reflections("com.devsoftec");
        Set<Class<? extends CommandHandler>> classes     = reflections.getSubTypesOf(CommandHandler.class);

        indexedCommandHandlers = formatHandlers(classes);
    }

    private Map<Class<? extends Command>, Class<? extends CommandHandler>> formatHandlers(
            Set<Class<? extends CommandHandler>> commandHandlers
    ) {
        Map<Class<? extends Command>, Class<? extends CommandHandler>> handlers = new HashMap<>();

        for (Class<? extends CommandHandler> handler : commandHandlers) {
            ParameterizedType paramType    = (ParameterizedType) handler.getGenericInterfaces()[0];
            Class<? extends Command> commandClass = (Class<? extends Command>) paramType.getActualTypeArguments()[0];

            handlers.put(commandClass, handler);
        }

        return handlers;
    }

    public Class<? extends CommandHandler> search(Class<? extends Command> commandClass) throws CommandNotRegisteredError {
        Class<? extends CommandHandler> commandHandlerClass = indexedCommandHandlers.get(commandClass);

        if (null == commandHandlerClass) {
            throw new CommandNotRegisteredError(commandClass);
        }

        return commandHandlerClass;
    }
}
