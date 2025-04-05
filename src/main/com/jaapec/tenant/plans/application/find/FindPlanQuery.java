package com.jaapec.tenant.plans.application.find;

import com.jaapec.tenant.shared.domain.bus.query.Query;

public record FindPlanQuery(String id) implements Query {}
