package com.jaapec.tenant.tenant.domain;

import java.util.Objects;

import com.jaapec.tenant.shared.domain.AggregateRoot;
import com.jaapec.tenant.shared.domain.CurrentDate;
import com.jaapec.tenant.tenant.domain.events.TenantCreatedDomainEvent;

public final class Tenant extends AggregateRoot {

	private final TenantId id;
	private final TenantName name;
	private final TenantStatus status;
	private final TenantCustomDomain customDomain;
	private final TenantDomainVerified domainVerified;
	private final TenantDomainHash domainHash;
	private final TenantOwnerId ownerId;
	private final TenantCreatedAt createdAt;
	private final TenantUpdatedAt updatedAt;

	public Tenant(
		TenantId id,
		TenantName name,
		TenantStatus status,
		TenantCustomDomain customDomain,
		TenantDomainVerified domainVerified,
		TenantDomainHash domainHash,
		TenantOwnerId ownerId,
		TenantCreatedAt createdAt,
		TenantUpdatedAt updatedAt
	) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.customDomain = customDomain;
		this.domainVerified = domainVerified;
		this.domainHash = domainHash;
		this.ownerId = ownerId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Tenant() {
		this.id = null;
		this.name = null;
		this.status = null;
		this.customDomain = null;
		this.domainVerified = null;
		this.domainHash = null;
		this.ownerId = null;
		this.createdAt = null;
		this.updatedAt = null;
	}

	public TenantId id() {
		return id;
	}

	public TenantName name() {
		return name;
	}

	public TenantStatus status() {
		return status;
	}

	public TenantCustomDomain customDomain() {
		return customDomain;
	}

	public TenantDomainVerified domainVerified() {
		return domainVerified;
	}

	public TenantDomainHash domainHash() {
		return domainHash;
	}

	public TenantOwnerId ownerId() {
		return ownerId;
	}

	public TenantCreatedAt createdAt() {
		return createdAt;
	}

	public TenantUpdatedAt updatedAt() {
		return updatedAt;
	}

	public static Tenant create(TenantId id, TenantName name, TenantOwnerId ownerId) {
		String now = CurrentDate.now();
		Tenant tenant = new Tenant(
			id,
			name,
			new TenantStatus(TenantStatus.Status.PENDING.toString()),
			null,
			null,
			null,
			ownerId,
			new TenantCreatedAt(now),
			new TenantUpdatedAt(now)
		);
		tenant.record(
			new TenantCreatedDomainEvent(
				id.value(),
				name.value(),
				ownerId.value(),
				tenant.createdAt.value(),
				tenant.updatedAt.value()
			)
		);
		return tenant;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Tenant tenant = (Tenant) o;
		return (
			Objects.requireNonNull(id).equals(tenant.id) &&
			Objects.requireNonNull(name).equals(tenant.name) &&
			Objects.requireNonNull(status).equals(tenant.status) &&
			Objects.equals(customDomain, tenant.customDomain) &&
			Objects.equals(domainVerified, tenant.domainVerified) &&
			Objects.equals(domainHash, tenant.domainHash) &&
			Objects.requireNonNull(ownerId).equals(tenant.ownerId)
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.requireNonNull(id).hashCode();
		result = 31 * result + Objects.requireNonNull(name).hashCode();
		result = 31 * result + Objects.requireNonNull(status).hashCode();
		result = 31 * result + Objects.hashCode(customDomain);
		result = 31 * result + Objects.hashCode(domainVerified);
		result = 31 * result + Objects.hashCode(domainHash);
		result = 31 * result + Objects.requireNonNull(ownerId).hashCode();
		return result;
	}
}
