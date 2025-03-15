package com.devsoftec.jaap.users.users.domain;

import com.devsoftec.jaap.users.shared.domain.ValueObjects.Identifier;

public final class UserId extends Identifier {

	public UserId(String value) {
		super(value);
	}

	public UserId() {
		super(null);
	}
}
