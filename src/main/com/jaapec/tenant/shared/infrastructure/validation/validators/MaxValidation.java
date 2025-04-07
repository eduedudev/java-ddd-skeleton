package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
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

		if (Objects.requireNonNull(rule).contains(":") && rule.split(":")[0].equals("max")) {
			int max = Integer.parseInt(rule.split(":")[1]);
			if (fieldValue instanceof Number number) {
				return number.doubleValue() <= max;
			}
			if (fieldValue instanceof String string) {
				return string.length() <= max;
			}
		}
		return false;
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.max.invalid", new Object[] { fieldName, Objects.requireNonNull(rule).split(":")[1] });
	}
}
