package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.MotherCreator;

public final class UserEmailMother {

	public static UserEmail create(String value) {
		return new UserEmail(value);
	}

	public static UserEmail random() {
		return new UserEmail(MotherCreator.random().internet().emailAddress());
	}
}
