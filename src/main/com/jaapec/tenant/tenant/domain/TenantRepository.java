package com.jaapec.tenant.tenant.domain;

import java.util.List;
import java.util.Optional;

import com.jaapec.tenant.shared.domain.Repository;
import com.jaapec.tenant.shared.domain.criteria.Criteria;

public interface TenantRepository extends Repository {
	void save(Tenant tenant);

	void delete(Tenant tenant);

	void update(Tenant tenant);

	Optional<Tenant> find(TenantId id);

	List<Tenant> matching(Criteria criteria);

	long count(Criteria criteria);
}
