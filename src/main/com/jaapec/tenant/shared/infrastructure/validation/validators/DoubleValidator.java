package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Repository;

public final class DoubleValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		HashMap<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	) {
		return fields.get(fieldName) instanceof Double;
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return "The field " + fieldName + " must be a double";
	}
}
