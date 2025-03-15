package com.devsoftec.jaap.users.users.domain;

import java.util.Objects;

import com.devsoftec.jaap.users.shared.domain.AggregateRoot;
import com.devsoftec.jaap.users.users.domain.events.UserCreatedDomainEvent;

public final class User extends AggregateRoot {

	private final UserId id;
	private final UserName name;
	private final UserEmail email;

	public User(UserId id, UserName name, UserEmail email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public User() {
		this.id = null;
		this.name = null;
		this.email = null;
	}

	public UserId id() {
		return id;
	}

	public UserName name() {
		return name;
	}

	public UserEmail email() {
		return email;
	}

	public static User create(UserId id, UserName name, UserEmail email) {
		User user = new User(id, name, email);
		user.record(new UserCreatedDomainEvent(id.value(), name.value(), email.value()));
		return user;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;
		return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(email);
		return result;
	}
}
