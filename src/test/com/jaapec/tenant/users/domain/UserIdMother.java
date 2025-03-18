package com.jaapec.tenant.users.domain;

import com.jaapec.tenant.shared.domain.UuidMother;

public final class UserIdMother {

	public static UserId create(String value) {
		return new UserId(value);
	}

	public static UserId random() {
		return create(UuidMother.generate());
	}
}
