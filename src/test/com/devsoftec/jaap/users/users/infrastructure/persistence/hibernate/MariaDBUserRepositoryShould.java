package com.devsoftec.jaap.users.users.infrastructure.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.devsoftec.jaap.users.users.UsersModuleInfrastructureTestCase;
import com.devsoftec.jaap.users.users.domain.User;
import com.devsoftec.jaap.users.users.domain.UserIdMother;
import com.devsoftec.jaap.users.users.domain.UserMother;

@Transactional
class MariaDBUserRepositoryShould extends UsersModuleInfrastructureTestCase {

	@Test
	void save_a_user() {
		User user = UserMother.random();

		mariadbUserRepository.save(user);
	}

	@Test
	void return_an_existing_user() {
		User user = UserMother.random();

		mariadbUserRepository.save(user);

		assertEquals(Optional.of(user), mariadbUserRepository.search(user.id()));
	}

	@Test
	void not_return_a_non_existing_user() {
		assertFalse(mariadbUserRepository.search(UserIdMother.random()).isPresent());
	}
}
