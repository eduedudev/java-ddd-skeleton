package com.jaapec.tenant.plans.domain;

import java.util.List;
import java.util.Optional;

import com.jaapec.tenant.plans.domain.value_objects.PlanId;
import com.jaapec.tenant.shared.domain.Repository;
import com.jaapec.tenant.shared.domain.criteria.Criteria;

public interface PlanRepository extends Repository {
	void save(Plan plan);

	void delete(Plan plan);

	void update(Plan plan);

	Optional<Plan> find(PlanId id);

	List<Plan> matching(Criteria criteria);

	long count(Criteria criteria);
}
