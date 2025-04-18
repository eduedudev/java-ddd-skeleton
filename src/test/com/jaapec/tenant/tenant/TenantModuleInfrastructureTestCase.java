package com.jaapec.tenant.tenant;

import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.infrastructure.InfrastructureTestCase;
import com.jaapec.tenant.tenant.domain.TenantRepository;

public abstract class TenantModuleInfrastructureTestCase extends InfrastructureTestCase {

	@Autowired
	protected TenantRepository mariadbTenantRepository;
}
