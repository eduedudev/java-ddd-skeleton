package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public final class IntegerValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		HashMap<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	) {
		return fields.get(fieldName) instanceof Integer;
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.integer.invalid", new Object[] { fieldName });
	}
}
