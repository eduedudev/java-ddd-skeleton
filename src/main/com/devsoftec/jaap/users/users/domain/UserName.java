package com.devsoftec.jaap.users.users.domain;

import com.devsoftec.jaap.users.shared.domain.ValueObjects.StringValueObject;

public final class UserName extends StringValueObject {

	public UserName() {
		super(null);
	}

	public UserName(String value) {
		super(value);
	}
}
