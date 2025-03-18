package com.jaapec.tenant.users.application.create;

import com.jaapec.tenant.shared.domain.ResourceAlreadyExists;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;
import com.jaapec.tenant.users.domain.*;

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
