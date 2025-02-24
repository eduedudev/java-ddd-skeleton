package com.devsoftec.jaap.users.shared.domain.criteria;

public record Order(OrderBy orderBy, OrderType orderType) {
	public static Order fromValues(String orderBy, String orderType) {
		OrderBy orderByObj = orderBy == null || orderBy.isEmpty() ? new OrderBy("") : new OrderBy(orderBy);
		OrderType orderTypeObj = OrderType.valueOf(orderType == null || orderType.isEmpty() ? "ASC" : orderType);

		return new Order(orderByObj, orderTypeObj);
	}

	private static Order none() {
		return new Order(new OrderBy(""), OrderType.NONE);
	}

	public boolean hasOrder() {
		return !orderType.isNone();
	}
}
