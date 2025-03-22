package com.jaapec.tenant.Plans.domain;

import java.util.Objects;

import com.jaapec.tenant.Plans.domain.ValueObjects.*;

public final class Plan {

	private final PlanId id;
	private final PlanName name;
	private final PlanDescription description;
	private final PlanPriceMonthly priceMonthly;
	private final PlanPriceYearly priceYearly;
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
		PlanPriceMonthly priceMonthly,
		PlanPriceYearly priceYearly,
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

	public Plan() {
		this.id = null;
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

	public PlanId id() {
		return id;
	}

	public PlanName name() {
		return name;
	}

	public PlanDescription description() {
		return description;
	}

	public PlanPriceMonthly priceMonthly() {
		return priceMonthly;
	}

	public PlanPriceYearly priceYearly() {
		return priceYearly;
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

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Plan plan = (Plan) o;
		return (
			Objects.equals(id, plan.id) &&
			Objects.equals(name, plan.name) &&
			Objects.equals(description, plan.description) &&
			Objects.equals(priceMonthly, plan.priceMonthly) &&
			Objects.equals(priceYearly, plan.priceYearly) &&
			Objects.equals(maxUsers, plan.maxUsers) &&
			Objects.equals(maxRoles, plan.maxRoles) &&
			Objects.equals(maxAccounts, plan.maxAccounts) &&
			Objects.equals(maxInvoices, plan.maxInvoices) &&
			Objects.equals(status, plan.status) &&
			Objects.equals(visibility, plan.visibility) &&
			Objects.equals(trialDays, plan.trialDays) &&
			Objects.equals(createdAt, plan.createdAt) &&
			Objects.equals(updatedAt, plan.updatedAt)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(name);
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
		result = 31 * result + Objects.hashCode(createdAt);
		result = 31 * result + Objects.hashCode(updatedAt);
		return result;
	}
}
