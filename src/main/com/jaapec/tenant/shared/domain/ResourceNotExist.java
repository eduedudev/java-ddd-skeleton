package com.jaapec.tenant.shared.domain;

public final class ResourceNotExist extends DomainError {

	private static final String ERROR_CODE = "E404";
	private static final String MESSAGE_KEY = "error.resource.not.found";

	/**
	 * Create a new {@code ResourceNotExist} with the specified reason and value
	 *
	 * @param reason the reason for the error
	 * @param value the value that caused the error
	 */
	public ResourceNotExist(String reason, String value) {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { reason }), reason, value);
	}
}
