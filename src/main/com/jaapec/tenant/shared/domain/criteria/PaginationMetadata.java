package com.jaapec.tenant.shared.domain.criteria;

public record PaginationMetadata(
	int currentPage,
	int totalPages,
	long totalItems,
	boolean hasNext,
	boolean hasPrevious
) {
	public static PaginationMetadata calculateMetadata(Pagination pagination, long totalItems) {
		int currentPage = (pagination.offset() / pagination.limit()) + 1;
		int totalPages = (int) Math.ceil((double) totalItems / pagination.limit());

		return new PaginationMetadata(
			currentPage,
			totalPages,
			totalItems,
			hasNextPage(pagination, totalItems),
			hasPreviousPage(pagination)
		);
	}

	private static boolean hasNextPage(Pagination pagination, long totalItems) {
		return (pagination.offset() + pagination.limit()) < totalItems;
	}

	private static boolean hasPreviousPage(Pagination pagination) {
		return pagination.offset() > 0;
	}
}
