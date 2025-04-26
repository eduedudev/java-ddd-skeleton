package com.jaapec.tenant.tenant.application.update;

import com.jaapec.tenant.tenant.domain.TenantId;
import com.jaapec.tenant.tenant.domain.TenantName;
import com.jaapec.tenant.tenant.domain.TenantNameMother;

public final class UpdateTenantCommandMother {

	public static UpdateTenantCommand create(TenantId id, TenantName name) {
		return new UpdateTenantCommand(id.value(), name.value());
	}

	public static UpdateTenantCommand random(TenantId id) {
		return create(id, TenantNameMother.random());
	}
}
