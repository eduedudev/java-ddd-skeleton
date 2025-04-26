package com.jaapec.tenant.tenant.application.update;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record UpdateTenantCommand(String id, String name) implements Command {}
