package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.value_objects.BooleanValueObject;

public class TenantDomainVerified extends BooleanValueObject {

	public TenantDomainVerified(Boolean value) {
		super(value);
	}

	public TenantDomainVerified() {
		super(null);
	}
}
