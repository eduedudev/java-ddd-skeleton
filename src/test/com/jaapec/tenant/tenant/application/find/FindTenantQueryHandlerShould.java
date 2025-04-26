package com.jaapec.tenant.tenant.application.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.tenant.TenantModuleUnitTestCase;
import com.jaapec.tenant.tenant.application.TenantResponse;
import com.jaapec.tenant.tenant.application.TenantResponseMother;
import com.jaapec.tenant.tenant.domain.CreateTenantCommandMother;
import com.jaapec.tenant.tenant.domain.Tenant;
import com.jaapec.tenant.tenant.domain.TenantIdMother;
import com.jaapec.tenant.tenant.domain.TenantMother;

final class FindTenantQueryHandlerShould extends TenantModuleUnitTestCase {

	private FindTenantQueryHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new FindTenantQueryHandler(new TenantFinder(repository));
	}

	@Test
	void return_tenant_when_it_exists() {
		Tenant tenant = TenantMother.fromRequest(CreateTenantCommandMother.random());
		FindTenantQuery query = new FindTenantQuery(tenant.id().value());
		TenantResponse response = TenantResponseMother.create(tenant);
		shouldSearch(tenant);
		assertEquals(response, handler.handle(query));
	}

	@Test
	void return_not_found_when_tenant_does_not_exist() {
		FindTenantQuery query = new FindTenantQuery(TenantIdMother.random().value());
		shouldSearch();
		assertThrows(ResourceNotExist.class, () -> handler.handle(query));
	}
}
