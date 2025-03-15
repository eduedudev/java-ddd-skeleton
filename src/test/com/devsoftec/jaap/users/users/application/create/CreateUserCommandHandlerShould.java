package com.devsoftec.jaap.users.users.application.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsoftec.jaap.users.users.UsersModuleUnitTestCase;
import com.devsoftec.jaap.users.users.domain.User;
import com.devsoftec.jaap.users.users.domain.UserCreatedDomainEventMother;
import com.devsoftec.jaap.users.users.domain.UserMother;
import com.devsoftec.jaap.users.users.domain.events.UserCreatedDomainEvent;

class CreateUserCommandHandlerShould extends UsersModuleUnitTestCase {

	private CreateUserCommandHandler handler;

	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new CreateUserCommandHandler(new UserCreator(repository, eventBus));
	}

	@Test
	void create_a_valid_user() {
		CreateUserCommand command = CreateUserCommandMother.random();

		User course = UserMother.fromRequest(command);
		UserCreatedDomainEvent domainEvent = UserCreatedDomainEventMother.fromCourse(course);

		handler.handle(command);

		shouldHaveSaved(course);
		shouldHavePublished(domainEvent);
	}
}
