package com.jaapec.tenant.tenant.application.cancel_auto_renew;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record CancelAutoRenewCommand(String tenantId, String subscriptionId) implements Command {}
