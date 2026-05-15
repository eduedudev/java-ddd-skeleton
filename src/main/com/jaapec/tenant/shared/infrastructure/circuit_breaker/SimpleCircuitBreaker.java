package com.jaapec.tenant.shared.infrastructure.circuit_breaker;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.jaapec.tenant.shared.domain.circuit_breaker.CircuitBreaker;
import com.jaapec.tenant.shared.domain.circuit_breaker.CircuitBreakerState;

public final class SimpleCircuitBreaker implements CircuitBreaker {

	private final AtomicReference<CircuitBreakerState> state = new AtomicReference<>(CircuitBreakerState.CLOSED);
	private final AtomicInteger failureCount = new AtomicInteger(0);
	private final int failureThreshold;
	private final long timeoutMs;
	private final int halfOpenMaxCalls;
	private final AtomicInteger halfOpenCallCount = new AtomicInteger(0);
	private volatile long lastFailureTime;

	public SimpleCircuitBreaker(int failureThreshold, long timeoutMs, int halfOpenMaxCalls) {
		this.failureThreshold = failureThreshold;
		this.timeoutMs = timeoutMs;
		this.halfOpenMaxCalls = halfOpenMaxCalls;
	}

	public SimpleCircuitBreaker() {
		this(3, 30_000, 1);
	}

	@Override
	public <T> T call(Supplier<T> supplier, Supplier<T> fallback) {
		CircuitBreakerState currentState = state.get();

		if (currentState == CircuitBreakerState.OPEN) {
			if (System.currentTimeMillis() - lastFailureTime >= timeoutMs) {
				if (tryTransitionToHalfOpen()) {
					return attemptHalfOpenCall(supplier, fallback);
				}
			}
			return fallback.get();
		}

		try {
			T result = supplier.get();
			onSuccess();
			return result;
		} catch (Exception e) {
			onFailure();
			return fallback.get();
		}
	}

	@Override
	public void call(Runnable runnable, Runnable fallback) {
		CircuitBreakerState currentState = state.get();

		if (currentState == CircuitBreakerState.OPEN) {
			if (System.currentTimeMillis() - lastFailureTime >= timeoutMs) {
				if (tryTransitionToHalfOpen()) {
					attemptHalfOpenCall(runnable, fallback);
					return;
				}
			}
			fallback.run();
			return;
		}

		try {
			runnable.run();
			onSuccess();
		} catch (Exception e) {
			onFailure();
			fallback.run();
		}
	}

	private synchronized boolean tryTransitionToHalfOpen() {
		if (state.get() == CircuitBreakerState.OPEN) {
			state.set(CircuitBreakerState.HALF_OPEN);
			halfOpenCallCount.set(0);
			return true;
		}
		return false;
	}

	private <T> T attemptHalfOpenCall(Supplier<T> supplier, Supplier<T> fallback) {
		if (halfOpenCallCount.incrementAndGet() > halfOpenMaxCalls) {
			return fallback.get();
		}

		try {
			T result = supplier.get();
			onSuccess();
			return result;
		} catch (Exception e) {
			onFailure();
			return fallback.get();
		}
	}

	private void attemptHalfOpenCall(Runnable runnable, Runnable fallback) {
		if (halfOpenCallCount.incrementAndGet() > halfOpenMaxCalls) {
			fallback.run();
			return;
		}

		try {
			runnable.run();
			onSuccess();
		} catch (Exception e) {
			onFailure();
			fallback.run();
		}
	}

	private synchronized void onSuccess() {
		failureCount.set(0);
		halfOpenCallCount.set(0);
		state.set(CircuitBreakerState.CLOSED);
	}

	private synchronized void onFailure() {
		lastFailureTime = System.currentTimeMillis();

		if (state.get() == CircuitBreakerState.HALF_OPEN) {
			state.set(CircuitBreakerState.OPEN);
			return;
		}

		if (failureCount.incrementAndGet() >= failureThreshold) {
			state.set(CircuitBreakerState.OPEN);
		}
	}

	@Override
	public CircuitBreakerState state() {
		return state.get();
	}

	@Override
	public void reset() {
		state.set(CircuitBreakerState.CLOSED);
		failureCount.set(0);
		halfOpenCallCount.set(0);
		lastFailureTime = 0;
	}
}
