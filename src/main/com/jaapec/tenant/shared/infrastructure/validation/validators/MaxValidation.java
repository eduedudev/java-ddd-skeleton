package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Repository;

public class MaxValidation implements FieldValidator {

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

		assert rule != null;
		if (rule.contains(":") && rule.split(":")[0].equals("max")) {
			int max = Integer.parseInt(rule.split(":")[1]);
			return fieldValue.toString().length() <= max;
		}
		return false;
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format(
			"The field %s must be greater than or equal to %s",
			fieldName,
			Objects.requireNonNull(rule).split(":")[1]
		);
	}
}
