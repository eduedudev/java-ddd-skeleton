package com.jaapec.tenant.shared.domain;

public final class NameMother {

	public static String random() {
		String name;
		do {
			name = MotherCreator.random().lorem().word();
		} while (name.length() < 4);
		return name;
	}
}
