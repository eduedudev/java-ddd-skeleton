package com.jaapec.tenant.shared.domain;

import net.datafaker.Faker;

public final class MotherCreator {

	private static final Faker faker = new Faker();

	public static Faker random() {
		return faker;
	}
}
