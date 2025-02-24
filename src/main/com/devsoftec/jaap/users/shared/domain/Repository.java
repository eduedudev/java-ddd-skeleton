package com.devsoftec.jaap.users.shared.domain;

public interface Repository {
	boolean uniqueField(String fieldName, String value);
}
