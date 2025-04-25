package com.jaapec.tenant.tenant.application.search;

import java.util.List;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.criteria.*;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Service
public final class TenantSearcher {

	private final TenantRepository repository;

	public TenantSearcher(TenantRepository repository) {
		this.repository = repository;
	}

	public PaginatedResponse<TenantResponse> search(Filters filters, Order order, Pagination pagination) {
		Criteria criteria = new Criteria(filters, order, pagination);
		long total = repository.count(criteria);

		List<TenantResponse> plans = repository.matching(criteria).stream().map(TenantResponse::fromAggregate).toList();

		PaginationMetadata metadata = PaginationMetadata.calculateMetadata(pagination, total);

		return new PaginatedResponse<>(plans, metadata);
	}
}
