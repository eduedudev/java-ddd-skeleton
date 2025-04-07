package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public class MaxValidation implements FieldValidator {

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

		assert rule != null;
		if (rule.contains(":") && rule.split(":")[0].equals("max")) {
			int max = Integer.parseInt(rule.split(":")[1]);
			if (fieldValue instanceof Number) {
				return ((Number) fieldValue).doubleValue() <= max;
			}
			if (fieldValue instanceof String) {
				return fieldValue.toString().length() <= max;
			}
		}
		return false;
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.max.invalid", new Object[] { fieldName, Objects.requireNonNull(rule).split(":")[1] });
	}
}
