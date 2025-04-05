package com.jaapec.tenant.plans.application.search;

import java.util.List;

import com.jaapec.tenant.shared.domain.bus.query.Query;
import com.jaapec.tenant.shared.domain.criteria.Filter;
import com.jaapec.tenant.shared.domain.criteria.Pagination;

public record SearchPlanQuery(List<Filter> filters, String orderBy, String orderType, Pagination pagination)
	implements Query {}
