package com.jaapec.tenant.shared.domain;

public abstract class DomainError extends RuntimeException {

	private final String errorCode;
	private final Message errorMessage;
	private final String reason;
	private final String value;

	/**
	 * Create a new {@code DomainError} with the specified error code, error
	 *
	 * @param errorCode the error code
	 * @param errorMessage the error message
	 * @param reason the reason for the error
	 * @param value the value that caused the error
	 */
	protected DomainError(String errorCode, Message errorMessage, String reason, String value) {
		super(errorMessage.messageKey());
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.reason = reason;
		this.value = value;
	}

	protected DomainError(String errorCode, Message errorMessage) {
		super(errorMessage.messageKey());
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.reason = null;
		this.value = null;
	}

	public String errorCode() {
		return errorCode;
	}

	public Message errorMessage() {
		return errorMessage;
	}

	public String reason() {
		return reason;
	}

	public String value() {
		return value;
	}
}
