package com.jaapec.tenant.tenant.domain;

import java.util.Objects;

public final class TenantDomain {

	private final String value;

	public TenantDomain(String value) {
		this.value = value;
		ensureDomain();
	}

	public TenantDomain() {
		this.value = null;
	}

	private void ensureDomain() {
		if (value == null) {
			return;
		}

		if (value.startsWith("-") || value.startsWith(".") || value.endsWith("-") || value.endsWith(".")) {
			throw new InvalidDomainException("domain", value);
		}

		String[] parts = value.split("\\.");
		if (parts.length < 2) {
			throw new InvalidDomainException("domain", value);
		}

		for (String part : parts) {
			if (part.isEmpty() || part.length() > 63) {
				throw new InvalidDomainException("domain", value);
			}
			if (!part.matches("^[A-Za-z0-9-]+$")) {
				throw new InvalidDomainException("domain", value);
			}
			if (part.startsWith("-") || part.endsWith("-")) {
				throw new InvalidDomainException("domain", value);
			}
		}

		String tld = parts[parts.length - 1];
		if (!tld.matches("^[A-Za-z]{2,}$")) {
			throw new InvalidDomainException("domain", value);
		}
	}

	public String value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		TenantDomain that = (TenantDomain) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
