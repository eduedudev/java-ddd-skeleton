package com.jaapec.tenant.tenant.application.change_domain;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record ChangeTenantDomainCommand(String id, String domain) implements Command {}
