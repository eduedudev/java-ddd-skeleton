package com.devsoftec.jaap.users.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

import jakarta.annotation.Nullable;

import com.devsoftec.jaap.users.shared.domain.Repository;

public class EmailValidator implements FieldValidator {

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

		String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

		Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

		if (fieldValue instanceof String && emailPattern.matcher((String) fieldValue).matches()) {
			return true;
		}

		return false;
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format("The field %s should be of type email", fieldName);
	}
}
