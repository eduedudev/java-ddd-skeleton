package com.jaapec.tenant.shared.domain;

public final class ResourceAlreadyExists extends DomainError {

	private static final String ERROR_CODE = "E409";
	private static final String MESSAGE_KEY = "error.resource.exists";

	/**
	 * Create a new {@code ResourceAlreadyExists} with the specified reason, unique field and value
	 *
	 * @param reason the reason for the error
	 * @param uniqueField the unique field
	 * @param value the value that caused the error
	 */
	public ResourceAlreadyExists(String reason, String uniqueField, String value) {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { reason, uniqueField, value }), uniqueField, value);
	}
}
