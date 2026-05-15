package com.jaapec.tenant.shared.infrastructure.circuit_breaker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.shared.domain.circuit_breaker.CircuitBreaker;
import com.jaapec.tenant.shared.domain.circuit_breaker.CircuitBreakerState;

class SimpleCircuitBreakerShould {

	private static final int THRESHOLD = 3;
	private static final long TIMEOUT_MS = 100;
	private static final int HALF_OPEN_MAX = 1;

	private CircuitBreaker circuitBreaker;

	@BeforeEach
	void setUp() {
		circuitBreaker = new SimpleCircuitBreaker(THRESHOLD, TIMEOUT_MS, HALF_OPEN_MAX);
	}

	@Test
	void start_in_closed_state() {
		assertEquals(CircuitBreakerState.CLOSED, circuitBreaker.state());
	}

	@Test
	void execute_supplier_when_closed() {
		boolean result = circuitBreaker.call(() -> true, () -> false);

		assertTrue(result);
		assertEquals(CircuitBreakerState.CLOSED, circuitBreaker.state());
	}

	@Test
	void execute_fallback_after_supplier_fails() {
		boolean result = circuitBreaker.call(
			() -> {
				throw new RuntimeException("fail");
			},
			() -> false
		);

		assertFalse(result);
	}

	@Test
	void transition_to_open_after_threshold_failures() {
		for (int i = 0; i < THRESHOLD; i++) {
			circuitBreaker.call(
				() -> {
					throw new RuntimeException("fail");
				},
				() -> false
			);
		}

		assertEquals(CircuitBreakerState.OPEN, circuitBreaker.state());
	}

	@Test
	void call_fallback_directly_when_circuit_is_open() {
		for (int i = 0; i < THRESHOLD; i++) {
			circuitBreaker.call(
				() -> {
					throw new RuntimeException("fail");
				},
				() -> false
			);
		}

		AtomicInteger supplierCalls = new AtomicInteger(0);
		boolean result = circuitBreaker.call(
			() -> {
				supplierCalls.incrementAndGet();
				return true;
			},
			() -> false
		);

		assertFalse(result);
		assertEquals(0, supplierCalls.get());
	}

	@Test
	void remain_closed_after_successful_call() {
		circuitBreaker.call(() -> true, () -> false);
		circuitBreaker.call(() -> true, () -> false);
		circuitBreaker.call(() -> true, () -> false);

		assertEquals(CircuitBreakerState.CLOSED, circuitBreaker.state());
	}

	@Test
	void reset_failure_count_on_success() {
		circuitBreaker.call(
			() -> {
				throw new RuntimeException("fail");
			},
			() -> false
		);
		circuitBreaker.call(
			() -> {
				throw new RuntimeException("fail");
			},
			() -> false
		);
		circuitBreaker.call(() -> true, () -> false);
		circuitBreaker.call(
			() -> {
				throw new RuntimeException("fail");
			},
			() -> false
		);
		circuitBreaker.call(
			() -> {
				throw new RuntimeException("fail");
			},
			() -> false
		);
		circuitBreaker.call(
			() -> {
				throw new RuntimeException("fail");
			},
			() -> false
		);

		assertEquals(CircuitBreakerState.OPEN, circuitBreaker.state());
	}

	@Test
	void transition_to_half_open_after_timeout() throws Exception {
		for (int i = 0; i < THRESHOLD; i++) {
			circuitBreaker.call(
				() -> {
					throw new RuntimeException("fail");
				},
				() -> false
			);
		}

		Thread.sleep(TIMEOUT_MS + 50);

		circuitBreaker.call(
			() -> {
				throw new RuntimeException("still failing");
			},
			() -> false
		);

		assertEquals(CircuitBreakerState.OPEN, circuitBreaker.state());
	}

	@Test
	void transition_to_closed_after_half_open_success() throws Exception {
		for (int i = 0; i < THRESHOLD; i++) {
			circuitBreaker.call(
				() -> {
					throw new RuntimeException("fail");
				},
				() -> false
			);
		}

		Thread.sleep(TIMEOUT_MS + 50);

		boolean result = circuitBreaker.call(() -> true, () -> false);

		assertTrue(result);
		assertEquals(CircuitBreakerState.CLOSED, circuitBreaker.state());
	}

	@Test
	void stay_closed_after_consecutive_successes_in_half_open() throws Exception {
		for (int i = 0; i < THRESHOLD; i++) {
			circuitBreaker.call(
				() -> {
					throw new RuntimeException("fail");
				},
				() -> false
			);
		}

		Thread.sleep(TIMEOUT_MS + 50);

		circuitBreaker.call(() -> true, () -> false);
		circuitBreaker.call(() -> true, () -> false);

		assertEquals(CircuitBreakerState.CLOSED, circuitBreaker.state());
	}

	@Test
	void execute_runnable_when_closed() {
		AtomicInteger executed = new AtomicInteger(0);

		circuitBreaker.call(() -> executed.incrementAndGet(), () -> {});

		assertEquals(1, executed.get());
	}

	@Test
	void execute_runnable_fallback_when_circuit_is_open() {
		for (int i = 0; i < THRESHOLD; i++) {
			circuitBreaker.call(
				() -> {
					throw new RuntimeException("fail");
				},
				() -> {}
			);
		}

		AtomicInteger supplierCalls = new AtomicInteger(0);
		AtomicInteger fallbackCalls = new AtomicInteger(0);

		circuitBreaker.call(() -> supplierCalls.incrementAndGet(), () -> fallbackCalls.incrementAndGet());

		assertEquals(0, supplierCalls.get());
		assertEquals(1, fallbackCalls.get());
	}

	@Test
	void reset_returns_to_closed_state() {
		for (int i = 0; i < THRESHOLD; i++) {
			circuitBreaker.call(
				() -> {
					throw new RuntimeException("fail");
				},
				() -> false
			);
		}

		assertEquals(CircuitBreakerState.OPEN, circuitBreaker.state());

		circuitBreaker.reset();

		assertEquals(CircuitBreakerState.CLOSED, circuitBreaker.state());
		assertDoesNotThrow(() -> circuitBreaker.call(() -> true, () -> false));
	}

	@Test
	void supplier_result_is_returned() {
		String result = circuitBreaker.call(() -> "hello", () -> "fallback");

		assertEquals("hello", result);
	}

	@Test
	void fallback_result_is_returned_on_failure() {
		String result = circuitBreaker.call(
			() -> {
				throw new RuntimeException();
			},
			() -> "fallback"
		);

		assertEquals("fallback", result);
	}

	@Test
	void handle_multiple_calls_race_condition() throws Exception {
		AtomicInteger failures = new AtomicInteger(0);
		AtomicReference<CircuitBreakerState> observedState = new AtomicReference<>();

		Thread breaker = new Thread(() -> {
			for (int i = 0; i < THRESHOLD; i++) {
				circuitBreaker.call(
					() -> {
						throw new RuntimeException();
					},
					() -> failures.incrementAndGet()
				);
			}
			observedState.set(circuitBreaker.state());
		});

		breaker.start();
		breaker.join();

		assertEquals(CircuitBreakerState.OPEN, observedState.get());
	}

	@Test
	void default_constructor_uses_reasonable_defaults() {
		CircuitBreaker defaultBreaker = new SimpleCircuitBreaker();

		assertEquals(CircuitBreakerState.CLOSED, defaultBreaker.state());
		assertDoesNotThrow(() -> defaultBreaker.call(() -> true, () -> false));
	}
}
