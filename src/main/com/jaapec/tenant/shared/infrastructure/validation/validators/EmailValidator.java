package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public final class EmailValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		Map<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	) {
		Serializable fieldValue = fields.get(fieldName);

		if (fieldValue == null) {
			return true;
		}

		final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

		Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

		return (fieldValue instanceof String strFieldValue) && emailPattern.matcher(strFieldValue).matches();
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.email.invalid", new Object[] { fieldName });
	}
}
