package com.jaapec.tenant.plans.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jaapec.tenant.plans.domain.events.ChangeVisibilityPlanDomainEvent;
import com.jaapec.tenant.plans.domain.events.PlanCreatedDomainEvent;
import com.jaapec.tenant.plans.domain.events.PlanUpdatedDomainEvent;
import com.jaapec.tenant.plans.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.AggregateRoot;
import com.jaapec.tenant.shared.domain.CurrentDate;

public final class Plan extends AggregateRoot {

	private final PlanId id;
	private final PlanName name;
	private final PlanDescription description;
	private final List<PlanPrice> prices;
	private final PlanMaxUsers maxUsers;
	private final PlanMaxRoles maxRoles;
	private final PlanMaxAccounts maxAccounts;
	private final PlanMaxInvoices maxInvoices;
	private final PlanStatus status;
	private final PlanVisibility visibility;
	private final PlanTrialDays trialDays;
	private final PlanCreatedAt createdAt;
	private final PlanUpdatedAt updatedAt;

	public Plan(
		PlanId id,
		PlanName name,
		PlanDescription description,
		List<PlanPrice> prices,
		PlanMaxUsers maxUsers,
		PlanMaxRoles maxRoles,
		PlanMaxAccounts maxAccounts,
		PlanMaxInvoices maxInvoices,
		PlanStatus status,
		PlanVisibility visibility,
		PlanTrialDays trialDays,
		PlanCreatedAt createdAt,
		PlanUpdatedAt updatedAt
	) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.prices = prices;
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

	public Plan() {
		this.id = null;
		this.name = null;
		this.description = null;
		this.prices = null;
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

	public PlanId id() {
		return id;
	}

	public PlanName name() {
		return name;
	}

	public PlanDescription description() {
		return description;
	}

	public List<PlanPrice> prices() {
		return prices;
	}

	public PlanMaxUsers maxUsers() {
		return maxUsers;
	}

	public PlanMaxRoles maxRoles() {
		return maxRoles;
	}

	public PlanMaxAccounts maxAccounts() {
		return maxAccounts;
	}

	public PlanMaxInvoices maxInvoices() {
		return maxInvoices;
	}

	public PlanStatus status() {
		return status;
	}

	public PlanVisibility visibility() {
		return visibility;
	}

	public PlanTrialDays trialDays() {
		return trialDays;
	}

	public PlanCreatedAt createdAt() {
		return createdAt;
	}

	public PlanUpdatedAt updatedAt() {
		return updatedAt;
	}

	public static Plan create(
		PlanId id,
		PlanName name,
		PlanDescription description,
		PlanMaxUsers maxUsers,
		PlanMaxRoles maxRoles,
		PlanMaxAccounts maxAccounts,
		PlanMaxInvoices maxInvoices,
		PlanStatus status,
		PlanVisibility visibility,
		PlanTrialDays trialDays
	) {
		String now = CurrentDate.now();
		Plan plan = new Plan(
			id,
			name,
			description,
			List.of(),
			maxUsers,
			maxRoles,
			maxAccounts,
			maxInvoices,
			status,
			visibility,
			trialDays,
			new PlanCreatedAt(now),
			new PlanUpdatedAt(now)
		);
		plan.record(
			new PlanCreatedDomainEvent(
				id.value(),
				name.value(),
				description.value(),
				maxUsers.value().toString(),
				maxRoles.value().toString(),
				maxAccounts.value().toString(),
				maxInvoices.value().toString(),
				status.value(),
				visibility.value(),
				trialDays.value().toString(),
				plan.createdAt().value(),
				plan.updatedAt().value()
			)
		);
		return plan;
	}

	public Plan update(
		PlanName name,
		PlanDescription description,
		PlanMaxUsers maxUsers,
		PlanMaxRoles maxRoles,
		PlanMaxAccounts maxAccounts,
		PlanMaxInvoices maxInvoices,
		PlanStatus status,
		PlanVisibility visibility,
		PlanTrialDays trialDays
	) {
		String now = CurrentDate.now();
		Plan plan = new Plan(
			this.id,
			name,
			description,
			this.prices,
			maxUsers,
			maxRoles,
			maxAccounts,
			maxInvoices,
			status,
			visibility,
			trialDays,
			this.createdAt,
			new PlanUpdatedAt(now)
		);
		plan.record(
			new PlanUpdatedDomainEvent(
				plan.id().value(),
				name.value(),
				description.value(),
				maxUsers.value().toString(),
				maxRoles.value().toString(),
				maxAccounts.value().toString(),
				maxInvoices.value().toString(),
				status.value(),
				visibility.value(),
				trialDays.value().toString(),
				plan.createdAt().value(),
				plan.updatedAt().value()
			)
		);
		return plan;
	}

	public Plan changeVisibility(PlanVisibility visibility) {
		String now = CurrentDate.now();
		Plan updatedPlan = new Plan(
			this.id,
			this.name,
			this.description,
			this.prices,
			this.maxUsers,
			this.maxRoles,
			this.maxAccounts,
			this.maxInvoices,
			this.status,
			visibility,
			this.trialDays,
			this.createdAt,
			new PlanUpdatedAt(now)
		);
		updatedPlan.record(
			new ChangeVisibilityPlanDomainEvent(
				updatedPlan.id().value(),
				updatedPlan.visibility().value(),
				updatedPlan.updatedAt().value()
			)
		);
		return updatedPlan;
	}

	public Plan addPrice(PlanPriceId id, BillingInterval billingInterval, Amount amount, Currency currency) {
		String now = CurrentDate.now();
		List<PlanPrice> newPrices = new ArrayList<>(Optional.ofNullable(this.prices).map(List::copyOf).orElseGet(List::of));
		PlanPrice newPrice = new PlanPrice(
			id,
			billingInterval,
			amount,
			currency,
			this,
			new PlanPriceCreatedAt(now),
			new PlanPriceUpdatedAt(now)
		);

		newPrices.add(newPrice);

		return new Plan(
			this.id,
			this.name,
			this.description,
			newPrices,
			this.maxUsers,
			this.maxRoles,
			this.maxAccounts,
			this.maxInvoices,
			this.status,
			this.visibility,
			this.trialDays,
			this.createdAt,
			new PlanUpdatedAt(now)
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Plan plan = (Plan) o;
		return (
			Objects.equals(id, plan.id) &&
			Objects.equals(name, plan.name) &&
			Objects.equals(description, plan.description) &&
			Objects.equals(prices, plan.prices) &&
			Objects.equals(maxUsers, plan.maxUsers) &&
			Objects.equals(maxRoles, plan.maxRoles) &&
			Objects.equals(maxAccounts, plan.maxAccounts) &&
			Objects.equals(maxInvoices, plan.maxInvoices) &&
			Objects.equals(status, plan.status) &&
			Objects.equals(visibility, plan.visibility) &&
			Objects.equals(trialDays, plan.trialDays)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(description);
		result = 31 * result + Objects.hashCode(prices);
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
