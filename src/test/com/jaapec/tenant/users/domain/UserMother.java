package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.users.application.create.CreateUserCommand;

public final class UserMother {

	public static User create(UserId id, UserName name, UserEmail email) {
		return new User(id, name, email);
	}

	public static User fromRequest(CreateUserCommand command) {
		return create(
			UserIdMother.create(command.id()),
			UserNameMother.create(command.name()),
			UserEmailMother.create(command.email())
		);
	}

	public static User random() {
		return create(UserIdMother.random(), UserNameMother.random(), UserEmailMother.random());
	}
}
