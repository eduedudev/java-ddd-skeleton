package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.Map;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public class UniqueFieldValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		Map<String, Serializable> fields,
		Repository repository,
		@Nullable String rule
	) {
		Serializable fieldValue = fields.get(fieldName);

		if (fieldValue == null) {
			return false;
		}

		return repository.uniqueField(fieldName, fieldValue.toString());
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.unique.invalid", new Object[] { fieldName });
	}
}
