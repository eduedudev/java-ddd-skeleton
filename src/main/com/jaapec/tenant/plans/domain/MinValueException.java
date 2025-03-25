package com.jaapec.tenant.plans.domain;

import com.jaapec.tenant.shared.domain.DomainError;

public class MinValueException extends DomainError {

	/**
	 * Create a new {@code MinValueException} with the specified reason and value
	 *
	 * @param reason the reason for the error
	 * @param value  the value that caused the error
	 */
	public MinValueException(String reason, String value) {
		super("E421", String.format("The %s must be greater than %s", reason, value), reason, value);
	}
}
