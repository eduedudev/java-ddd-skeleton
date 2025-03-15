package com.devsoftec.jaap.users.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.annotation.Nullable;

import com.devsoftec.jaap.users.shared.domain.Repository;

public class EnumValidator implements FieldValidator {

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
		if (rule.contains(":") && rule.split(":")[0].equals("enum")) {
			String[] enumValues = rule.split(":")[1].split(",");
			for (String enumValue : enumValues) {
				if (enumValue.equals(fieldValue.toString())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format("The field %s should be of type enum", fieldName);
	}
}
