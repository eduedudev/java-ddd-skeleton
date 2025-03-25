package com.jaapec.tenant.tenants.domain;

import java.util.Objects;

import com.jaapec.tenant.plans.domain.Plan;

public final class Tenant {

	private final TenantId id;
	private final TenantName name;
	private final Plan plan;
	private final TenantStatus status;
	private final TenantOwnerId ownerId;
	private final TenantDateSubscribed dateSubscribed;
	private final TenantExpirationDate expirationDate;
	private final TenantGracePeriodDays gracePeriod;
	private final TenantAutoRenew autoRenew;
	private final TenantCreatedAt createdAt;
	private final TenantUpdatedAt updatedAt;

	public Tenant(
		TenantId id,
		TenantName name,
		Plan plan,
		TenantStatus status,
		TenantOwnerId ownerId,
		TenantDateSubscribed dateSubscribed,
		TenantExpirationDate expirationDate,
		TenantGracePeriodDays gracePeriod,
		TenantAutoRenew autoRenew,
		TenantCreatedAt createdAt,
		TenantUpdatedAt updatedAt
	) {
		this.id = id;
		this.name = name;
		this.plan = plan;
		this.status = status;
		this.ownerId = ownerId;
		this.dateSubscribed = dateSubscribed;
		this.expirationDate = expirationDate;
		this.gracePeriod = gracePeriod;
		this.autoRenew = autoRenew;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Tenant() {
		this.id = null;
		this.name = null;
		this.plan = null;
		this.status = null;
		this.ownerId = null;
		this.dateSubscribed = null;
		this.expirationDate = null;
		this.gracePeriod = null;
		this.autoRenew = null;
		this.createdAt = null;
		this.updatedAt = null;
	}

	public TenantId id() {
		return id;
	}

	public TenantName name() {
		return name;
	}

	public Plan plan() {
		return plan;
	}

	public TenantStatus status() {
		return status;
	}

	public TenantOwnerId ownerId() {
		return ownerId;
	}

	public TenantDateSubscribed dateSubscribed() {
		return dateSubscribed;
	}

	public TenantExpirationDate expirationDate() {
		return expirationDate;
	}

	public TenantGracePeriodDays gracePeriod() {
		return gracePeriod;
	}

	public TenantAutoRenew autoRenew() {
		return autoRenew;
	}

	public TenantCreatedAt createdAt() {
		return createdAt;
	}

	public TenantUpdatedAt updatedAt() {
		return updatedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Tenant tenant = (Tenant) o;
		return (
			Objects.equals(id, tenant.id) &&
			Objects.equals(name, tenant.name) &&
			Objects.equals(plan, tenant.plan) &&
			Objects.equals(dateSubscribed, tenant.dateSubscribed) &&
			Objects.equals(status, tenant.status) &&
			Objects.equals(ownerId, tenant.ownerId) &&
			Objects.equals(expirationDate, tenant.expirationDate) &&
			Objects.equals(gracePeriod, tenant.gracePeriod) &&
			Objects.equals(autoRenew, tenant.autoRenew) &&
			Objects.equals(createdAt, tenant.createdAt) &&
			Objects.equals(updatedAt, tenant.updatedAt)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(plan);
		result = 31 * result + Objects.hashCode(dateSubscribed);
		result = 31 * result + Objects.hashCode(status);
		result = 31 * result + Objects.hashCode(ownerId);
		result = 31 * result + Objects.hashCode(expirationDate);
		result = 31 * result + Objects.hashCode(gracePeriod);
		result = 31 * result + Objects.hashCode(autoRenew);
		result = 31 * result + Objects.hashCode(createdAt);
		result = 31 * result + Objects.hashCode(updatedAt);
		return result;
	}
}
