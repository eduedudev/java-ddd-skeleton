package com.jaapec.tenant.healt_checker.infrastructure.rest;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class HealthCheckGetController {

	@GetMapping("/health")
	public Map<String, String> index() {
		return Map.of("status", "ok");
	}
}
