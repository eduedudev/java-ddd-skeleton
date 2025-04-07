package com.jaapec.tenant.users;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;

import com.jaapec.tenant.users.domain.User;
import com.jaapec.tenant.users.domain.UserRepository;
import com.jaapec.tenant.users.infrastructure.UnitTestCase;

public abstract class UsersModuleUnitTestCase extends UnitTestCase {

	protected UserRepository repository;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		repository = mock(UserRepository.class);
	}

	public void shouldHaveSaved(User user) {
		verify(repository, atLeastOnce()).save(user);
	}
}
