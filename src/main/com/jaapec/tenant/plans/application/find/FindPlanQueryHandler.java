package com.jaapec.tenant.plans.application.find;

import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.domain.ValueObjects.PlanId;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;

@Service
public final class FindPlanQueryHandler implements QueryHandler<FindPlanQuery, PlanResponse> {

	private final PlanFinder finder;

	public FindPlanQueryHandler(PlanFinder finder) {
		this.finder = finder;
	}

	@Override
	public PlanResponse handle(FindPlanQuery query) {
		return finder.find(new PlanId(query.id()));
	}
}
