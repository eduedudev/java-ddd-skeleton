package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public class TenantCustomDomain extends StringValueObject {

	public TenantCustomDomain(String value) {
		super(value);
	}

	public TenantCustomDomain() {
		super(null);
	}
}
