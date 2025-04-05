package com.jaapec.tenant.shared.domain.criteria;

public record Criteria(Filters filters, Order order, Pagination pagination) {
	public boolean hasFilters() {
		return !filters.filters().isEmpty();
	}
}
