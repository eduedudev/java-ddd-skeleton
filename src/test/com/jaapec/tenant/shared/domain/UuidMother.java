package com.jaapec.tenant.shared.domain;

import java.util.UUID;

public final class UuidMother {

	public static String generate() {
		return UUID.randomUUID().toString();
	}
}
