package com.devsoftec.jaap.users.shared.domain;

public final class ResourceNotExist extends DomainError {

	public ResourceNotExist(String resource, String reason) {
		super("E404", String.format("The %s doesn't exist", resource), reason);
	}
}
