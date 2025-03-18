package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Repository;

public class DateTimeValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		HashMap<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	) {
		Serializable fieldValue = fields.get(fieldName);

		if (fieldValue == null) {
			return true;
		}

		try {
			java.time.LocalDateTime.parse(fieldValue.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format("The field %s is not a valid datetime", fieldName);
	}
}
