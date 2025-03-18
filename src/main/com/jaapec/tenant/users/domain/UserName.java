package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.StringValueObject;

public final class UserName extends StringValueObject {

	public UserName() {
		super(null);
	}

	public UserName(String value) {
		super(value);
	}
}
