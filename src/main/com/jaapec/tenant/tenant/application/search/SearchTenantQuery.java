package com.jaapec.tenant.tenant.application.search;

import java.util.List;

import com.jaapec.tenant.shared.domain.bus.query.Query;
import com.jaapec.tenant.shared.domain.criteria.Filter;
import com.jaapec.tenant.shared.domain.criteria.Pagination;

public record SearchTenantQuery(List<Filter> filters, String orderBy, String orderType, Pagination pagination)
	implements Query {}
