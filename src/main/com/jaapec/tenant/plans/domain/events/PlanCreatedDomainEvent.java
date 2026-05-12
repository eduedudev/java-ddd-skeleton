package com.jaapec.tenant.plans.domain.events;

import java.io.Serializable;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class PlanCreatedDomainEvent extends PlanDomainEvent {

	public PlanCreatedDomainEvent(
		String name,
		String description,
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
		super(
			null,
			name,
			description,
			maxUsers,
			maxRoles,
			maxAccounts,
			maxInvoices,
			status,
			visibility,
			trialDays,
			createdAt,
			updatedAt
		);
	}

	public PlanCreatedDomainEvent(
		String aggregateId,
		String name,
		String description,
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
		super(
			aggregateId,
			name,
			description,
			maxUsers,
			maxRoles,
			maxAccounts,
			maxInvoices,
			status,
			visibility,
			trialDays,
			createdAt,
			updatedAt
		);
	}

	public PlanCreatedDomainEvent(
		String aggregateId,
		String eventId,
		String occurredOn,
		String name,
		String description,
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
		super(
			aggregateId,
			eventId,
			occurredOn,
			name,
			description,
			maxUsers,
			maxRoles,
			maxAccounts,
			maxInvoices,
			status,
			visibility,
			trialDays,
			createdAt,
			updatedAt
		);
	}

	PlanCreatedDomainEvent() {
		super(null, null, null, null, null, null, null, null, null, null, null, null);
	}

	@Override
	public String eventName() {
		return "plan.created";
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new PlanCreatedDomainEvent(
			aggregateId,
			eventId,
			occurredOn,
			(String) body.get("name"),
			(String) body.get("description"),
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
}
