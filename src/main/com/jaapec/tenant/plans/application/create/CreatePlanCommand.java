package com.jaapec.tenant.plans.application.create;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record CreatePlanCommand(
	String id,
	String name,
	String description,
	double priceMonthly,
	double priceYearly,
	int maxUsers,
	int maxRoles,
	int maxAccounts,
	int maxInvoices,
	String status,
	String visibility,
	int trialDays
)
	implements Command {}
