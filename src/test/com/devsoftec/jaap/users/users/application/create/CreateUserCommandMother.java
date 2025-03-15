package com.devsoftec.jaap.users.users.application.create;

import com.devsoftec.jaap.users.users.domain.*;

public final class CreateUserCommandMother {

	public static CreateUserCommand create(UserId id, UserName name, UserEmail email) {
		return new CreateUserCommand(id.value(), name.value(), email.value());
	}

	public static CreateUserCommand random() {
		return create(UserIdMother.random(), UserNameMother.random(), UserEmailMother.random());
	}
}
