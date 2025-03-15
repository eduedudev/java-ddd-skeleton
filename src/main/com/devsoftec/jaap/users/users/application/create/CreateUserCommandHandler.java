package com.devsoftec.jaap.users.users.application.create;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.bus.command.CommandHandler;
import com.devsoftec.jaap.users.users.domain.UserEmail;
import com.devsoftec.jaap.users.users.domain.UserId;
import com.devsoftec.jaap.users.users.domain.UserName;

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
