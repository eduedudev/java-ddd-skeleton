package com.jaapec.tenant.plans.application.search;

import java.util.List;

import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.criteria.*;

@Service
public final class PlanSearcher {

	private final PlanRepository repository;

	public PlanSearcher(PlanRepository repository) {
		this.repository = repository;
	}

	public PaginatedResponse<PlanResponse> search(Filters filters, Order order, Pagination pagination) {
		Criteria criteria = new Criteria(filters, order, pagination);
		long total = repository.count(criteria);

		List<PlanResponse> plans = repository.matching(criteria).stream().map(PlanResponse::fromAggregate).toList();

		PaginationMetadata metadata = PaginationMetadata.calculateMetadata(pagination, total);

		return new PaginatedResponse<>(plans, metadata);
	}
}
