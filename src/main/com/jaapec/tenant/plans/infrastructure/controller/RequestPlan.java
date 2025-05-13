package com.jaapec.tenant.plans.infrastructure.controller;

public record RequestPlan(
	String name,
	String description,
	int maxUsers,
	int maxRoles,
	int maxAccounts,
	int maxInvoices,
	String status,
	String visibility,
	int trialDays
) {}
