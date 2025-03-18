package com.jaapec.tenant.users;

import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.infrastructure.InfrastructureTestCase;
import com.jaapec.tenant.users.domain.UserRepository;

public abstract class UsersModuleInfrastructureTestCase extends InfrastructureTestCase {

	@Autowired
	protected UserRepository mariadbUserRepository;
}
