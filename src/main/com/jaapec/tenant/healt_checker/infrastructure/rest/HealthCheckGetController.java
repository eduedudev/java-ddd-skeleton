package com.jaapec.tenant.healt_checker.infrastructure.rest;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class HealthCheckGetController {

	@GetMapping("/health")
	public HashMap<String, String> index() {
		HashMap<String, String> status = new HashMap<>();
		status.put("status", "ok");

		return status;
	}
}
