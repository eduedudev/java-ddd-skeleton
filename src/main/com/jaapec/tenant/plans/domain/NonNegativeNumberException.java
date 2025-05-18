package com.jaapec.tenant.plans.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public final class NonNegativeNumberException extends DomainError {

	private static final String ERROR_CODE = "E422";
	private static final String MESSAGE_KEY = "error.resource.negative";

	/**
	 * Create a new {@code NonNegativeNumberException} with the specified reason and value
	 *
	 * @param reason the reason for the error
	 * @param value the value that caused the error
	 */
	public NonNegativeNumberException(String reason, String value) {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { reason }), reason, value);
	}
}
