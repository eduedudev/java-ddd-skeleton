package com.jaapec.tenant.plans.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public abstract class PlanDomainEvent extends DomainEvent {

	protected final String name;
	protected final String description;
	protected final String priceMonthly;
	protected final String priceYearly;
	protected final String maxUsers;
	protected final String maxRoles;
	protected final String maxAccounts;
	protected final String maxInvoices;
	protected final String status;
	protected final String visibility;
	protected final String trialDays;
	protected final String createdAt;
	protected final String updatedAt;

	protected PlanDomainEvent(
		String name,
		String description,
		String priceMonthly,
		String priceYearly,
		String maxUsers,
		String maxRoles,
		String maxAccounts,
		String maxInvoices,
		String status,
		String visibility,
		String trialDays,
		String createdAt,
		String updatedAt
	) {
		this.name = name;
		this.description = description;
		this.priceMonthly = priceMonthly;
		this.priceYearly = priceYearly;
		this.maxUsers = maxUsers;
		this.maxRoles = maxRoles;
		this.maxAccounts = maxAccounts;
		this.maxInvoices = maxInvoices;
		this.status = status;
		this.visibility = visibility;
		this.trialDays = trialDays;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	protected PlanDomainEvent(
		String aggregateId,
		String name,
		String description,
		String priceMonthly,
		String priceYearly,
		String maxUsers,
		String maxRoles,
		String maxAccounts,
		String maxInvoices,
		String status,
		String visibility,
		String trialDays,
		String createdAt,
		String updatedAt
	) {
		super(aggregateId);
		this.name = name;
		this.description = description;
		this.priceMonthly = priceMonthly;
		this.priceYearly = priceYearly;
		this.maxUsers = maxUsers;
		this.maxRoles = maxRoles;
		this.maxAccounts = maxAccounts;
		this.maxInvoices = maxInvoices;
		this.status = status;
		this.visibility = visibility;
		this.trialDays = trialDays;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	protected PlanDomainEvent(
		String aggregateId,
		String eventId,
		String occurredOn,
		String name,
		String description,
		String priceMonthly,
		String priceYearly,
		String maxUsers,
		String maxRoles,
		String maxAccounts,
		String maxInvoices,
		String status,
		String visibility,
		String trialDays,
		String createdAt,
		String updatedAt
	) {
		super(aggregateId, eventId, occurredOn);
		this.name = name;
		this.description = description;
		this.priceMonthly = priceMonthly;
		this.priceYearly = priceYearly;
		this.maxUsers = maxUsers;
		this.maxRoles = maxRoles;
		this.maxAccounts = maxAccounts;
		this.maxInvoices = maxInvoices;
		this.status = status;
		this.visibility = visibility;
		this.trialDays = trialDays;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	protected PlanDomainEvent() {
		super(null);
		this.name = null;
		this.description = null;
		this.priceMonthly = null;
		this.priceYearly = null;
		this.maxUsers = null;
		this.maxRoles = null;
		this.maxAccounts = null;
		this.maxInvoices = null;
		this.status = null;
		this.visibility = null;
		this.trialDays = null;
		this.createdAt = null;
		this.updatedAt = null;
	}

	@Override
	public Map<String, Serializable> toPrimitives() {
		Map<String, Serializable> primitives = new HashMap<>();
		primitives.put("name", name);
		primitives.put("description", description);
		primitives.put("priceMonthly", priceMonthly);
		primitives.put("priceYearly", priceYearly);
		primitives.put("maxUsers", maxUsers);
		primitives.put("maxRoles", maxRoles);
		primitives.put("maxAccounts", maxAccounts);
		primitives.put("maxInvoices", maxInvoices);
		primitives.put("status", status);
		primitives.put("visibility", visibility);
		primitives.put("trialDays", trialDays);
		primitives.put("createdAt", createdAt);
		primitives.put("updatedAt", updatedAt);
		return primitives;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		PlanDomainEvent that = (PlanDomainEvent) o;
		return (
			Objects.equals(name, that.name) &&
			Objects.equals(description, that.description) &&
			Objects.equals(priceMonthly, that.priceMonthly) &&
			Objects.equals(priceYearly, that.priceYearly) &&
			Objects.equals(maxUsers, that.maxUsers) &&
			Objects.equals(maxRoles, that.maxRoles) &&
			Objects.equals(maxAccounts, that.maxAccounts) &&
			Objects.equals(maxInvoices, that.maxInvoices) &&
			Objects.equals(status, that.status) &&
			Objects.equals(visibility, that.visibility) &&
			Objects.equals(trialDays, that.trialDays)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(description);
		result = 31 * result + Objects.hashCode(priceMonthly);
		result = 31 * result + Objects.hashCode(priceYearly);
		result = 31 * result + Objects.hashCode(maxUsers);
		result = 31 * result + Objects.hashCode(maxRoles);
		result = 31 * result + Objects.hashCode(maxAccounts);
		result = 31 * result + Objects.hashCode(maxInvoices);
		result = 31 * result + Objects.hashCode(status);
		result = 31 * result + Objects.hashCode(visibility);
		result = 31 * result + Objects.hashCode(trialDays);
		return result;
	}
}
