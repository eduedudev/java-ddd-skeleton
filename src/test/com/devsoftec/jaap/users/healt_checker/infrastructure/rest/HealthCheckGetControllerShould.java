package com.devsoftec.jaap.users.healt_checker.infrastructure.rest;

import com.devsoftec.jaap.users.RequestTestCase;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;

final class HealthGetControllerShould extends RequestTestCase {
    @Test
    void check_the_app_is_working_ok() throws Exception {
        assertResponse("/health", 200, "{'status':'ok'}");
    }
}