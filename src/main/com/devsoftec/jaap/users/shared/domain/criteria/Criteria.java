package com.devsoftec.jaap.users.shared.domain.criteria;

import java.util.Optional;

public record Criteria(Filters filters, Order order, Optional<Integer> limit, Optional<Integer> offset) {
    public boolean hasFilters() {
        return !filters.filters().isEmpty();
    }
}

