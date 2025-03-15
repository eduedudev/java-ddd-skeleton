package com.devsoftec.jaap.users.users.domain;

import com.devsoftec.jaap.users.users.domain.events.UserCreatedDomainEvent;

public final class UserCreatedDomainEventMother {

	public static UserCreatedDomainEvent create(UserId id, UserName name, UserEmail email) {
		return new UserCreatedDomainEvent(id.value(), name.value(), email.value());
	}

	public static UserCreatedDomainEvent fromCourse(User course) {
		return create(course.id(), course.name(), course.email());
	}

	public static UserCreatedDomainEvent random() {
		return create(UserIdMother.random(), UserNameMother.random(), UserEmailMother.random());
	}
}
