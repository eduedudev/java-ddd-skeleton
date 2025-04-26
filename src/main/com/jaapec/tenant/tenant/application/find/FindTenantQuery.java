package com.jaapec.tenant.tenant.application.find;

import com.jaapec.tenant.shared.domain.bus.query.Query;

public record FindTenantQuery(String id) implements Query {}
