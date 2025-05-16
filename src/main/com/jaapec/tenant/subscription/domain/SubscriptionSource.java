package com.jaapec.tenant.subscription.domain;

import java.util.Objects;

public final class SubscriptionSource {

	private final String value;

	public SubscriptionSource(String value) {
		this.value = value;
	}

	public SubscriptionSource() {
		this.value = null;
	}

	public String value() {
		return SubscriptionSource.source.valueOf(value).toString();
	}

	public enum source {
		BACKOFFICE,
		SELF_SERVICE,
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		SubscriptionSource that = (SubscriptionSource) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
