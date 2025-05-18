package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public final class SubscriptionNotFound extends DomainError {

	private static final String ERROR_CODE = "E426";
	private static final String MESSAGE_KEY = "error.subscription.not.found";

	public SubscriptionNotFound() {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { "" }), "", "");
	}
}
