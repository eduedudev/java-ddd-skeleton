package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Repository;

public final class UuidValidator implements FieldValidator {

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
		Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

		return uuidPattern.matcher((String) fieldValue).matches();
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format("The field %s is not a valid uuid", fieldName);
	}
}
