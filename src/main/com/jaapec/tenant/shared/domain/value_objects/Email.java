package com.jaapec.tenant.shared.domain.value_objects;

import java.util.Objects;
import java.util.regex.Pattern;

public abstract class Email {

	protected final String value;

	protected Email(String value) {
		if (value != null) {
			ensureValidEmail(value);
			this.value = value.toLowerCase();
		} else {
			this.value = null;
		}
	}

	public String value() {
		return value;
	}

	private void ensureValidEmail(String value) {
		final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (value == null || !Pattern.matches(EMAIL_REGEX, value) || value.contains("..")) {
			throw new IllegalArgumentException("The email is invalid");
		}

		String domain = value.substring(value.indexOf('@') + 1);

		if (domain.startsWith(".") || domain.endsWith(".")) {
			throw new IllegalArgumentException("The email is invalid");
		}

		if (domain.startsWith("-") || domain.endsWith("-")) {
			throw new IllegalArgumentException("The email is invalid");
		}

		for (String part : domain.split("\\.")) {
			if (part.startsWith("-") || part.endsWith("-")) {
				throw new IllegalArgumentException("The email is invalid");
			}
		}
	}

	@Override
	public String toString() {
		return "Email{" + "value='" + value + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Email email = (Email) o;
		return Objects.equals(value, email.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
