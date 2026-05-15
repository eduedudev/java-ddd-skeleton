package com.jaapec.tenant.shared.domain.circuit_breaker;

import java.util.function.Supplier;

public interface CircuitBreaker {
	<T> T call(Supplier<T> supplier, Supplier<T> fallback);

	void call(Runnable runnable, Runnable fallback);

	CircuitBreakerState state();

	void reset();
}
