package com.jaapec.tenant.tenant.application.change_domain;

import com.jaapec.tenant.tenant.domain.*;

public final class ChangeTenantDomainCommandMother {

	public static ChangeTenantDomainCommand create(TenantId id, TenantDomain domain) {
		return new ChangeTenantDomainCommand(id.value(), domain.value());
	}

	public static ChangeTenantDomainCommand random(TenantId id) {
		return create(id, TenantDomainMother.random());
	}
}
