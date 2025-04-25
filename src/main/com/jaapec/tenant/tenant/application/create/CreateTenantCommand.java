package com.jaapec.tenant.tenant.application.create;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record CreateTenantCommand(String id, String name, String ownerId) implements Command {}
