package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public final class ActiveSubscriptionAlreadyExistsException extends DomainError {

	private static final String ERROR_CODE = "E424";
	private static final String MESSAGE_KEY = "error.subscription.active.already.exists";

	public ActiveSubscriptionAlreadyExistsException() {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { "" }), "", "");
	}
}
