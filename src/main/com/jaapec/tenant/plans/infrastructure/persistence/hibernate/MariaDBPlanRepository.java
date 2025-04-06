package com.jaapec.tenant.plans.infrastructure.persistence.hibernate;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;

import com.jaapec.tenant.plans.domain.Plan;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.ValueObjects.PlanId;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.criteria.Criteria;
import com.jaapec.tenant.shared.infrastructure.persistence.hibernate.HibernateRepository;

@Transactional
@Service
public class MariaDBPlanRepository extends HibernateRepository<Plan> implements PlanRepository {

	public MariaDBPlanRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Plan.class);
	}

	@Override
	public void save(Plan plan) {
		persist(plan);
	}

	@Override
	public void delete(PlanId id) {}

	@Override
	public void update(Plan plan) {
		merge(plan);
	}

	@Override
	public Optional<Plan> find(PlanId id) {
		return byId(id);
	}

	@Override
	public List<Plan> matching(Criteria criteria) {
		return byCriteria(criteria);
	}

	@Override
	public long count(Criteria criteria) {
		return countByCriteria(criteria);
	}

	@Override
	public boolean uniqueField(String fieldName, String value) {
		return isFieldValueUnique(fieldName, value);
	}
}
