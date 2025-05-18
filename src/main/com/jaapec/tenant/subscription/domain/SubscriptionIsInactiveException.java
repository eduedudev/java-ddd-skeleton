package com.jaapec.tenant.subscription.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public final class SubscriptionIsInactiveException extends DomainError {

	private static final String ERROR_CODE = "E428";
	private static final String MESSAGE_KEY = "error.subscription.inactive";

	public SubscriptionIsInactiveException() {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { "" }), "", "");
	}
}
