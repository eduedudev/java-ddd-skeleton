package com.jaapec.tenant.shared.domain;

public final class DuplicateFieldException extends DomainError {

	private static final String ERROR_CODE = "E410";
	private static final String MESSAGE_KEY = "error.field.duplicate";

	/**
	 * Constructs a new {@code DuplicateFieldException} with the specified reason and value.
	 *
	 * @param reason the reason for the error
	 * @param value the value that caused the error
	 */
	public DuplicateFieldException(String reason, String value) {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { reason, value }), reason, value);
	}
}
