package com.jaapec.tenant.users.application.create;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record CreateUserCommand(String id, String name, String email) implements Command {}
