package com.jaapec.tenant.shared.domain;

public final class ResourceNotExist extends DomainError {

	private static final String errorCode = "E404";
	private static final String messageKey = "error.resource.not.found";

	/**
	 * Create a new {@code ResourceNotExist} with the specified reason and value
	 *
	 * @param reason the reason for the error
	 * @param value the value that caused the error
	 */
	public ResourceNotExist(String reason, String value) {
		super(errorCode, new Message(messageKey, new Object[] { reason }), reason, value);
	}
}
