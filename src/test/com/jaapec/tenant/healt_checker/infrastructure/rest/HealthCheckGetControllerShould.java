package com.jaapec.tenant.healt_checker.infrastructure.rest;

import org.junit.jupiter.api.Test;

import com.jaapec.tenant.RequestTestCase;

final class HealthGetControllerShould extends RequestTestCase {

	@Test
	void check_the_app_is_working_ok() throws Exception {
		assertResponse("/health", 200, "{'status':'ok'}");
	}
}
