package com.jaapec.tenant.users.infrastructure.persistence;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.infrastructure.persistence.hibernate.HibernateRepository;
import com.jaapec.tenant.users.domain.User;
import com.jaapec.tenant.users.domain.UserId;
import com.jaapec.tenant.users.domain.UserRepository;

@Transactional
@Service
public class MariaDBUserRepository extends HibernateRepository<User> implements UserRepository {

	public MariaDBUserRepository(SessionFactory sessionFactory) {
		super(sessionFactory, User.class);
	}

	@Override
	public void save(User user) {
		persist(user);
	}

	@Override
	public Optional<User> search(UserId id) {
		return byId(id);
	}

	@Override
	public boolean uniqueField(String fieldName, String value) {
		return isFieldValueUnique(fieldName, value);
	}
}
