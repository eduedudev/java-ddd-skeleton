package com.devsoftec.jaap.users.users.domain;

import com.devsoftec.jaap.users.shared.domain.ValueObjects.Email;

public final class UserEmail extends Email {

	public UserEmail() {
		super(null);
	}

	public UserEmail(String value) {
		super(value);
	}
}
