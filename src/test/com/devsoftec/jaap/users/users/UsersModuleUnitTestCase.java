package com.devsoftec.jaap.users.users;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;

import com.devsoftec.jaap.users.users.domain.User;
import com.devsoftec.jaap.users.users.domain.UserRepository;
import com.devsoftec.jaap.users.users.infrastructure.UnitTestCase;

public abstract class UsersModuleUnitTestCase extends UnitTestCase {

	protected UserRepository repository;

	@BeforeEach
	protected void setUp() {
		super.setUp();

		repository = mock(UserRepository.class);
	}

	public void shouldHaveSaved(User user) {
		verify(repository, atLeastOnce()).save(user);
	}
}
