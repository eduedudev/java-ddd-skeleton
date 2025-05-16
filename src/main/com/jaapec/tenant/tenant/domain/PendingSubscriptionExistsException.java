package com.jaapec.tenant.tenant.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public class PendingSubscriptionExistsException extends DomainError {

	private static final String ERROR_CODE = "E425";
	private static final String MESSAGE_KEY = "error.subscription.pending.already.exists";

	public PendingSubscriptionExistsException() {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { "" }), "", "");
	}
}
