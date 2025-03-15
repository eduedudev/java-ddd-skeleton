package com.devsoftec.jaap.users.users.domain;

import java.util.Optional;

import com.devsoftec.jaap.users.shared.domain.Repository;

public interface UserRepository extends Repository {
	void save(User user);
	Optional<User> search(UserId id);
}
