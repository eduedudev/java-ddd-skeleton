package com.jaapec.tenant.shared.domain;

public final class ResourceAlreadyExists extends DomainError {

	/**
	 * Create a new {@code ResourceAlreadyExists} with the specified reason, unique field and value
	 *
	 * @param reason       the reason for the error
	 * @param uniqueField  the unique field
	 * @param value        the value that caused the error
	 */
	public ResourceAlreadyExists(String reason, String uniqueField, String value) {
		super("E409", String.format("The %s with %s %s already exists", reason, uniqueField, value), uniqueField, value);
	}
}
