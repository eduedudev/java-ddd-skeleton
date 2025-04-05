package com.jaapec.tenant.shared.domain.criteria;

import java.util.List;

public record Filters(List<Filter> filters) {
	public static Filters fromValues(List<Filter> filters) {
		return new Filters(filters);
	}
}
