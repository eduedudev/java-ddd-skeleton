package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.MotherCreator;

public final class UserNameMother {

	public static UserName create(String value) {
		return new UserName(value);
	}

	public static UserName random() {
		return create(MotherCreator.random().name().fullName());
	}
}
