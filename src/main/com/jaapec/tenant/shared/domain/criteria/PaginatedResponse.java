package com.jaapec.tenant.shared.domain.criteria;

import java.util.List;

import com.jaapec.tenant.shared.domain.bus.query.Response;

public record PaginatedResponse<T>(List<T> data, PaginationMetadata pagination) implements Response {}
