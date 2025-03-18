package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Repository;

public final class StringValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		HashMap<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	) {
		return true;
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format("The field %s should be of type string", fieldName);
	}
}
