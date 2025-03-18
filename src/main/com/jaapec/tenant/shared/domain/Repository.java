package com.jaapec.tenant.shared.domain;

public interface Repository {
	boolean uniqueField(String fieldName, String value);
}
