package com.jaapec.tenant.plans.application.change_visibility;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record ChangeVisibilityPlanCommand(String id, String visibility) implements Command {}
