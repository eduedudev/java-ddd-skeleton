package com.devsoftec.jaap.users.users.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.criteria.*;
import com.devsoftec.jaap.users.shared.infrastructure.persistence.hibernate.HibernateRepository;
import com.devsoftec.jaap.users.users.domain.User;
import com.devsoftec.jaap.users.users.domain.UserId;
import com.devsoftec.jaap.users.users.domain.UserRepository;

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
		Filter filter = new Filter(new FilterField(fieldName), FilterOperator.EQUAL, new FilterValue(value));
		Filters filters = new Filters(List.of(filter));

		Criteria criteria = new Criteria(filters, new Order(null, OrderType.NONE), Optional.empty(), Optional.empty());
		List<User> account = byCriteria(criteria);
		return account.isEmpty();
	}
}
