package com.jaapec.tenant.shared.domain.circuit_breaker;

public final class CircuitBreakerException extends RuntimeException {

	public CircuitBreakerException(String message) {
		super(message);
	}
}
