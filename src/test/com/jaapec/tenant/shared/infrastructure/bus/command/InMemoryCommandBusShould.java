package com.jaapec.tenant.shared.infrastructure.bus.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import com.jaapec.tenant.shared.domain.bus.command.Command;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandlerExecutionError;
import com.jaapec.tenant.shared.domain.bus.command.CommandNotRegisteredError;

class InMemoryCommandBusShould {

	private CommandHandlersInformation information;
	private ApplicationContext context;
	private InMemoryCommandBus commandBus;
	private TestCommandHandler handler;

	@BeforeEach
	void setUp() {
		information = mock(CommandHandlersInformation.class);
		context = mock(ApplicationContext.class);
		handler = spy(new TestCommandHandler());
		commandBus = new InMemoryCommandBus(information, context);
	}

	@Test
	void dispatch_a_command_to_its_handler() throws Exception {
		TestCommand command = new TestCommand();

		doReturn(TestCommandHandler.class).when(information).search(any());
		when(context.getBean(TestCommandHandler.class)).thenReturn(handler);

		commandBus.dispatch(command);

		verify(handler).handle(command);
	}

	@Test
	void throw_an_exception_when_command_has_no_handler() throws CommandNotRegisteredError {
		TestCommand command = new TestCommand();

		doThrow(new CommandNotRegisteredError(TestCommand.class)).when(information).search(any());

		assertThrows(CommandHandlerExecutionError.class, () -> commandBus.dispatch(command));
	}

	@Test
	void throw_an_exception_when_handler_throws_an_exception() throws CommandNotRegisteredError {
		TestCommand command = new TestCommand();

		doReturn(TestCommandHandler.class).when(information).search(any());
		when(context.getBean(TestCommandHandler.class)).thenReturn(handler);
		doThrow(new RuntimeException("Error")).when(handler).handle(command);

		assertThrows(CommandHandlerExecutionError.class, () -> commandBus.dispatch(command));
	}

	static class TestCommand implements Command {}

	static class TestCommandHandler implements CommandHandler<TestCommand> {

		@Override
		public void handle(TestCommand command) {
			// Do nothing
		}
	}
}
