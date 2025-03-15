package com.devsoftec.jaap.users.users;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsoftec.jaap.users.shared.infrastructure.InfrastructureTestCase;
import com.devsoftec.jaap.users.users.domain.UserRepository;

public abstract class UsersModuleInfrastructureTestCase extends InfrastructureTestCase {

	@Autowired
	protected UserRepository mariadbUserRepository;
}
