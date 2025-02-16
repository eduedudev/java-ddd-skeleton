package com.devsoftec.jaap.users.healt_checker.infrastructure.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
final public class HealthCheckGetController {
    @GetMapping("/health")
    public HashMap<String, String> index() {
        HashMap<String, String> status = new HashMap<>();
        status.put("status", "ok");

        return status;
    }
}
