package com.devsoftec.jaap.users.users.domain;

import com.devsoftec.jaap.users.shared.domain.MotherCreator;

public final class UserEmailMother {

	public static UserEmail create(String value) {
		return new UserEmail(value);
	}

	public static UserEmail random() {
		return new UserEmail(MotherCreator.random().internet().emailAddress());
	}
}
