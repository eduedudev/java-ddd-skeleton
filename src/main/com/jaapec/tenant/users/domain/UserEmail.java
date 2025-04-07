package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.value_objects.Email;

public final class UserEmail extends Email {

	public UserEmail() {
		super(null);
	}

	public UserEmail(String value) {
		super(value);
	}
}
