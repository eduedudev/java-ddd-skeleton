package com.jaapec.tenant.users.application.create;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.users.domain.UserEmail;
import com.jaapec.tenant.users.domain.UserId;
import com.jaapec.tenant.users.domain.UserName;

@Service
public final class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {

	private final UserCreator creator;

	public CreateUserCommandHandler(UserCreator creator) {
		this.creator = creator;
	}

	@Override
	public void handle(CreateUserCommand command) {
		creator.create(new UserId(command.id()), new UserName(command.name()), new UserEmail(command.email()));
	}
}
