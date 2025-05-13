package com.jaapec.tenant.plans.application.update;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record UpdatePlanCommand(
	String id,
	String name,
	String description,
	int maxUsers,
	int maxRoles,
	int maxAccounts,
	int maxInvoices,
	String status,
	String visibility,
	int trialDays
)
	implements Command {}
