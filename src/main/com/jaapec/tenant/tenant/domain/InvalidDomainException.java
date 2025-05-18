package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public final class InvalidDomainException extends DomainError {

	private static final String ERROR_CODE = "E423";
	private static final String MESSAGE_KEY = "error.invalid.domain";

	/**
	 * Exception thrown when the provided domain is invalid.
	 *
	 * @param reason the reason explaining why the domain is considered invalid
	 * @param value the specific invalid value that caused the exception
	 */
	public InvalidDomainException(String reason, String value) {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { reason }), reason, value);
	}
}
