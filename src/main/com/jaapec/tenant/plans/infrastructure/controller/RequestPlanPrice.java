package com.jaapec.tenant.plans.infrastructure.controller;

public record RequestPlanPrice(String billingInterval, int amount, String currency, String PlanId) {}
