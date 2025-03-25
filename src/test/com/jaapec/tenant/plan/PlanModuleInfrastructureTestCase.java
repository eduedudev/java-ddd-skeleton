package com.jaapec.tenant.plan;

import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.infrastructure.InfrastructureTestCase;

public abstract class PlanModuleInfrastructureTestCase extends InfrastructureTestCase {

	@Autowired
	protected PlanRepository mariadbPlanRepository;
}
