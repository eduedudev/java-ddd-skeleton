package com.devsoftec.jaap.users.users.domain;

import com.devsoftec.jaap.users.shared.domain.UuidMother;

public final class UserIdMother {

	public static UserId create(String value) {
		return new UserId(value);
	}

	public static UserId random() {
		return create(UuidMother.generate());
	}
}
