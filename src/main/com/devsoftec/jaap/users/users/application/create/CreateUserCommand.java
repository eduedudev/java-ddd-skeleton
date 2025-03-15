package com.devsoftec.jaap.users.users.application.create;

import com.devsoftec.jaap.users.shared.domain.bus.command.Command;

public record CreateUserCommand(String id, String name, String email) implements Command {}
