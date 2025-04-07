package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.value_objects.Identifier;

public final class UserId extends Identifier {

	public UserId(String value) {
		super(value);
	}

	public UserId() {
		super(null);
	}
}
