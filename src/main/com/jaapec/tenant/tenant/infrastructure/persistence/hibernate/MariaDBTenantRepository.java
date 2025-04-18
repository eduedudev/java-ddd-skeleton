package com.jaapec.tenant.tenant.infrastructure.persistence.hibernate;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.criteria.Criteria;
import com.jaapec.tenant.shared.infrastructure.persistence.hibernate.HibernateRepository;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Transactional
@Service
public class MariaDBTenantRepository extends HibernateRepository<Tenant> implements TenantRepository {

	public MariaDBTenantRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Tenant.class);
	}

	@Override
	public void save(Tenant Tenant) {
		persist(Tenant);
	}

	@Override
	public void delete(Tenant Tenant) {
		remove(Tenant);
	}

	@Override
	public void update(Tenant Tenant) {
		merge(Tenant);
	}

	@Override
	public Optional<Tenant> find(TenantId id) {
		return byId(id);
	}

	@Override
	public List<Tenant> matching(Criteria criteria) {
		return byCriteria(criteria);
	}

	@Override
	public long count(Criteria criteria) {
		return countByCriteria(criteria);
	}
}
