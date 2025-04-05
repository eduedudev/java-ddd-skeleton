package com.jaapec.tenant.shared.domain.criteria;

public record Pagination(int limit, int offset) {
	private static final int DEFAULT_LIMIT = 20;
	private static final int MAX_LIMIT = 100;

	public Pagination {
		limit = Math.min(limit > 0 ? limit : DEFAULT_LIMIT, MAX_LIMIT);
		offset = Math.max(offset - 1, 0);
	}

	public static Pagination defaults() {
		return new Pagination(0, DEFAULT_LIMIT);
	}

	public static Pagination fromValues(int limit, int offset) {
		return new Pagination(limit, offset);
	}
}
