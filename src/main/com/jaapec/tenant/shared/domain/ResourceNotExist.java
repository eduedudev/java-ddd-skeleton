package com.jaapec.tenant.shared.domain;

public final class ResourceNotExist extends DomainError {

	/**
	 * Create a new {@code ResourceNotExist} with the specified reason and value
	 *
	 * @param reason the reason for the error
	 * @param value  the value that caused the error
	 */
	public ResourceNotExist(String reason, String value) {
		super("E404", String.format("The %s doesn't exist", reason), reason, value);
	}
}
