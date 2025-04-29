package com.jaapec.tenant.tenant.application.verify_domain;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record VerifyTenantDomainCommand(String tenantId) implements Command {}
