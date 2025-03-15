package com.devsoftec.jaap.users.shared.domain;

public final class ResourceAlreadyExists extends DomainError {

	public ResourceAlreadyExists(String resource, String uniqueField, String value) {
		super("E409", String.format("The %s with %s %s already exists", resource, uniqueField, value), value);
	}
}
