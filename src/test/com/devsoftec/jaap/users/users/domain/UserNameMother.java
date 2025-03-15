package com.devsoftec.jaap.users.users.domain;

import com.devsoftec.jaap.users.shared.domain.MotherCreator;

public final class UserNameMother {

	public static UserName create(String value) {
		return new UserName(value);
	}

	public static UserName random() {
		return create(MotherCreator.random().name().fullName());
	}
}
