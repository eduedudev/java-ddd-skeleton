package com.jaapec.tenant.plans.application.delete;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record DeletePlanCommand(String id) implements Command {}
