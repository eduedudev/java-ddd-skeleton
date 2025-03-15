package com.devsoftec.jaap.users.shared.domain;

public final class WordMother {

	public static String random() {
		return MotherCreator.random().lorem().word();
	}
}
