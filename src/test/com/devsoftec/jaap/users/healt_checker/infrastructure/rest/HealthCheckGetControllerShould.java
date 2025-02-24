package com.devsoftec.jaap.users.healt_checker.infrastructure.rest;

import org.junit.jupiter.api.Test;

import com.devsoftec.jaap.users.RequestTestCase;

final class HealthGetControllerShould extends RequestTestCase {

	@Test
	void check_the_app_is_working_ok() throws Exception {
		assertResponse("/health", 200, "{'status':'ok'}");
	}
}
