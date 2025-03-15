package com.devsoftec.jaap.users.shared.domain.ValueObjects;

import java.util.Objects;
import java.util.regex.Pattern;

public abstract class Identifier {

	protected final String value;
	private static final Pattern UUID_PATTERN = Pattern.compile(
			"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
	);

	public Identifier(String value) {
		ensureValidUuid(value);
		this.value = value;
	}

	public String value() {
		return value;
	}

	private void ensureValidUuid(String value) throws IllegalArgumentException {
		if(value !=null)
			if (!UUID_PATTERN.matcher(value).matches())
				throw new IllegalArgumentException("Invalid UUID format: " + value);

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Identifier that = (Identifier) o;
		return value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
