package com.jaapec.tenant.shared.domain.criteria;

import javax.annotation.Nullable;

public record Order(String field, Direction direction) {
	public static Order from(@Nullable String field, @Nullable String direction) {
		return new Order(
			field != null ? field : "createdAt", // default field
			Direction.fromString(direction)
		);
	}
	public static Order defaultOrder() {
		return new Order("createdAt", Direction.DESC);
	}

	public boolean hasOrder() {
		return Direction.NONE != defaultOrder().direction();
	}

	public enum Direction {
		ASC,
		DESC,
		NONE;

		public boolean isAsc() {
			return ASC == this;
		}

		public boolean isDesc() {
			return DESC == this;
		}

		public static Direction fromString(@Nullable String value) {
			if (value == null) return NONE;
			try {
				return valueOf(value.toUpperCase());
			} catch (IllegalArgumentException e) {
				return NONE;
			}
		}
	}
}
