package com.jaapec.tenant.tenants.domain;

import java.util.List;
import java.util.Optional;

import com.jaapec.tenant.shared.domain.criteria.Criteria;

public interface TenantRepository {
	void save(Tenant tenant);

	void delete(TenantId id);

	void update(Tenant tenant);

	Optional<Tenant> findById(TenantId id);

	List<Tenant> matching(Criteria criteria);
}
