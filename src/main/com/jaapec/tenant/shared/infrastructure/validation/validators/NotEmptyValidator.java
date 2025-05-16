package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.Map;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public final class NotEmptyValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		Map<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	) {
		Serializable fieldValue = fields.get(fieldName);
		return fieldValue == null || !fieldValue.toString().isEmpty();
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.not_empty.invalid", new Object[] { fieldName });
	}
}
