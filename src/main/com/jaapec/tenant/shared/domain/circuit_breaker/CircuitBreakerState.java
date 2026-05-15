package com.jaapec.tenant.shared.domain.circuit_breaker;

public enum CircuitBreakerState {
	CLOSED,
	OPEN,
	HALF_OPEN,
}
