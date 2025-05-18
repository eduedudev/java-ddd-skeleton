package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.Map;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public final class DateValidator implements FieldValidator {

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

		try {
			java.time.LocalDate.parse(fieldValue.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.date.invalid", new Object[] { fieldName });
	}
}
