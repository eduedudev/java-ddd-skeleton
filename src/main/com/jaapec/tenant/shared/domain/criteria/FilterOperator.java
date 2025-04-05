package com.jaapec.tenant.shared.domain.criteria;

public enum FilterOperator {
	EQUAL("="),
	NOT_EQUAL("!="),
	GT(">"),
	LT("<"),
	CONTAINS("CONTAINS"),
	NOT_CONTAINS("NOT_CONTAINS");

	private final String operator;

	FilterOperator(String operator) {
		this.operator = operator;
	}

	public static FilterOperator fromValue(String value) {
		return switch (value) {
			case "=", "EQUALS" -> FilterOperator.EQUAL;
			case "!=", "NOT_EQUALS" -> FilterOperator.NOT_EQUAL;
			case ">", "GT" -> FilterOperator.GT;
			case "<", "LT" -> FilterOperator.LT;
			case "CONTAINS" -> FilterOperator.CONTAINS;
			case "NOT_CONTAINS" -> FilterOperator.NOT_CONTAINS;
			default -> throw new IllegalArgumentException(String.format("Invalid operator %s", value));
		};
	}

	public boolean isPositive() {
		return this != NOT_EQUAL && this != NOT_CONTAINS;
	}

	public String value() {
		return operator;
	}
}
