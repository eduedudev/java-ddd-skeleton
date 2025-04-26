package com.jaapec.tenant.tenant.application.find;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.domain.TenantId;

@Service
public final class FindTenantQueryHandler implements QueryHandler<FindTenantQuery, TenantResponse> {

	private final TenantFinder finder;

	public FindTenantQueryHandler(TenantFinder finder) {
		this.finder = finder;
	}

	@Override
	public TenantResponse handle(FindTenantQuery query) {
		return finder.find(new TenantId(query.id()));
	}
}
