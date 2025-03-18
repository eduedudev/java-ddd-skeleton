package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Repository;

public final class BigDecimalValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		HashMap<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	) {
		Serializable value = fields.get(fieldName);

		if (value == null) {
			return false;
		}

		try {
			new BigDecimal(value.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return "The field " + fieldName + " must be a big decimal";
	}
}
