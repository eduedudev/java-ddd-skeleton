package com.jaapec.tenant.plans.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class PlanCreatedDomainEvent extends DomainEvent {

	private final String name;
	private final String description;
	private final String priceMonthly;
	private final String priceYearly;
	private final String maxUsers;
	private final String maxRoles;
	private final String maxAccounts;
	private final String maxInvoices;
	private final String status;
	private final String visibility;
	private final String trialDays;
	private final String createdAt;
	private final String updatedAt;

	public PlanCreatedDomainEvent(
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
		super(null);
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

	public PlanCreatedDomainEvent(
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

	public PlanCreatedDomainEvent(
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

	public PlanCreatedDomainEvent() {
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
	public String eventName() {
		return "plan.created";
	}

	@Override
	public HashMap<String, Serializable> toPrimitives() {
		return new HashMap<String, Serializable>() {
			{
				put("name", name);
				put("description", description);
				put("priceMonthly", priceMonthly);
				put("priceYearly", priceYearly);
				put("maxUsers", maxUsers);
				put("maxRoles", maxRoles);
				put("maxAccounts", maxAccounts);
				put("maxInvoices", maxInvoices);
				put("status", status);
				put("visibility", visibility);
				put("trialDays", trialDays);
				put("createdAt", createdAt);
				put("updatedAt", updatedAt);
			}
		};
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		HashMap<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new PlanCreatedDomainEvent(
			aggregateId,
			eventId,
			occurredOn,
			(String) body.get("name"),
			(String) body.get("description"),
			(String) body.get("priceMonthly"),
			(String) body.get("priceYearly"),
			(String) body.get("maxUsers"),
			(String) body.get("maxRoles"),
			(String) body.get("maxAccounts"),
			(String) body.get("maxInvoices"),
			(String) body.get("status"),
			(String) body.get("visibility"),
			(String) body.get("trialDays"),
			(String) body.get("createdAt"),
			(String) body.get("updatedAt")
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		final PlanCreatedDomainEvent that = (PlanCreatedDomainEvent) o;
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
