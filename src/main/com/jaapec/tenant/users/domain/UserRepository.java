package com.jaapec.tenant.users.domain;

import java.util.Optional;

import com.jaapec.tenant.shared.domain.Repository;

public interface UserRepository extends Repository {
	void save(User user);
	Optional<User> search(UserId id);
}
