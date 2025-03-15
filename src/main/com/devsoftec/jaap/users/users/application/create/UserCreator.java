package com.devsoftec.jaap.users.users.application.create;

import com.devsoftec.jaap.users.shared.domain.ResourceAlreadyExists;
import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.bus.event.EventBus;
import com.devsoftec.jaap.users.users.domain.*;

@Service
public final class UserCreator {

	private final UserRepository repository;
	private final EventBus eventBus;

	public UserCreator(UserRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void create(UserId id, UserName name, UserEmail email) {
		repository
			.search(id)
			.ifPresent(user -> {
				throw new ResourceAlreadyExists("user", "id", id.value());
			});
		User user = User.create(id, name, email);
		repository.save(user);
		eventBus.publish(user.pullDomainEvents());
	}
}
