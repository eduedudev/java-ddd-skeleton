package com.jaapec.tenant.subcriptions.domain;

import java.util.Objects;

public final class SubscriptionStatus {

	private final String value;

	public SubscriptionStatus(String value) {
		this.value = value;
	}

	public SubscriptionStatus() {
		this.value = null;
	}

	public enum status {
		ACTIVE,
		INACTIVE,
	}

	public String value() {
		return SubscriptionStatus.status.valueOf(value).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		SubscriptionStatus that = (SubscriptionStatus) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
