package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.Email;

public final class UserEmail extends Email {

	public UserEmail() {
		super(null);
	}

	public UserEmail(String value) {
		super(value);
	}
}
