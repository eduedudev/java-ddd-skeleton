package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.ValueObjects.Identifier;

public final class UserId extends Identifier {

	public UserId(String value) {
		super(value);
	}

	public UserId() {
		super(null);
	}
}
