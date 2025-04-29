package com.jaapec.tenant.tenant.application.check_domain_verification;

import com.jaapec.tenant.shared.domain.bus.query.Query;

public record CheckDomainVerificationQuery(String tenantId) implements Query {}
