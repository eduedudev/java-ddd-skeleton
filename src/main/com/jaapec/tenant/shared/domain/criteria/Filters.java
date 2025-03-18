package com.jaapec.tenant.shared.domain.criteria;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Filters(List<Filter> filters) {
	public static Filters fromValues(List<Map<String, String>> filters) {
		return new Filters(filters.stream().map(Filter::fromValues).collect(Collectors.toList()));
	}
}
