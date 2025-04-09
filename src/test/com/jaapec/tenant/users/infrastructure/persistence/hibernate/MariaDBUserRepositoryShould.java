package com.jaapec.tenant.users.infrastructure.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.users.UsersModuleInfrastructureTestCase;
import com.jaapec.tenant.users.domain.User;
import com.jaapec.tenant.users.domain.UserIdMother;
import com.jaapec.tenant.users.domain.UserMother;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class MariaDBUserRepositoryShould extends UsersModuleInfrastructureTestCase {

	@Test
	void save_a_user() {
		User user = UserMother.random();

		assertDoesNotThrow(() -> mariadbUserRepository.save(user));
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
