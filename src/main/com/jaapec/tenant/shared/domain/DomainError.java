package com.jaapec.tenant.shared.domain;

public abstract class DomainError extends RuntimeException {

	private final String errorCode;
	private final String errorMessage;
	private final String reason;
	private final String value;

	/**
	 * Create a new {@code DomainError} with the specified error code, error
	 *
	 * @param errorCode    the error code
	 * @param errorMessage the error message
	 * @param reason       the reason for the error
	 * @param value        the value that caused the error
	 */
	public DomainError(String errorCode, String errorMessage, String reason, String value) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.reason = reason;
		this.value = value;
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

	public String value() {
		return value;
	}
}
