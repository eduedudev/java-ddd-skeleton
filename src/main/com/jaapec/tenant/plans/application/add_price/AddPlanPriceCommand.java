package com.jaapec.tenant.plans.application.add_price;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record AddPlanPriceCommand(String planId, String id, String billingInterval, int amount, String currency)
	implements Command {}
