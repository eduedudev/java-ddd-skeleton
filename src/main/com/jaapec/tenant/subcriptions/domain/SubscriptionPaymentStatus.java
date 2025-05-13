package com.jaapec.tenant.subcriptions.domain;

import java.util.Objects;

public final class SubscriptionPaymentStatus {

	private final String value;

	public SubscriptionPaymentStatus(String value) {
		this.value = value;
	}

	public enum status {
		PENDING,
		PAID,
	}

	public String value() {
		return SubscriptionPaymentStatus.status.valueOf(value).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		SubscriptionPaymentStatus that = (SubscriptionPaymentStatus) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
