package com.jaapec.tenant.plans.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public class MinValueException extends DomainError {

	private static final String ERROR_CODE = "E421";
	private static final String MESSAGE_KEY = "error.min.invalid";

	/**
	 * Create a new {@code MinValueException} with the specified reason and value
	 *
	 * @param reason the reason for the error
	 * @param value the value that caused the error
	 */
	public MinValueException(String reason, String value) {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { reason, value }), reason, value);
	}
}
