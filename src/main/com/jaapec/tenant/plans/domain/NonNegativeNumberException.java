package com.jaapec.tenant.plans.domain;

import com.jaapec.tenant.shared.domain.DomainError;

public class NonNegativeNumberException extends DomainError {

	/**
	 * Create a new {@code NonNegativeNumberException} with the specified reason and value
	 * @param reason the reason for the error
	 * @param value  the value that caused the error
	 */
	public NonNegativeNumberException(String reason, String value) {
		super("E422", String.format("The %s must be a non negative number", reason), reason, value);
	}
}
