package com.jaapec.tenant.shared.domain;

public abstract class DomainError extends RuntimeException {

	private final String errorCode;
	private final String errorMessage;
	private final String reason;

	public DomainError(String errorCode, String errorMessage, String reason) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.reason = reason;
	}

	public String errorCode() {
		return errorCode;
	}

	public String errorMessage() {
		return errorMessage;
	}

	public String reason() {
		return reason;
	}
}
