package com.jaapec.tenant.tenant.domain;

import java.util.Objects;
import java.util.regex.Pattern;

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
		String regex = "^(?!-)([A-Za-z0-9-]{1,63}\\.)+[A-Za-z]{2,}$";
		Pattern pattern = Pattern.compile(regex);

		if (value != null && !pattern.matcher(value).matches()) {
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
