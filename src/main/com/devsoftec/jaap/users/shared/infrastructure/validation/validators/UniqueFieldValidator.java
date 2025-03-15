package com.devsoftec.jaap.users.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.annotation.Nullable;

import com.devsoftec.jaap.users.shared.domain.Repository;

public class UniqueFieldValidator implements FieldValidator {

	@Override
	public Boolean isValid(
		String fieldName,
		HashMap<String, Serializable> fields,
		Repository repository,
		@Nullable String rule
	) {
		Serializable fieldValue = fields.get(fieldName);

		if (fieldValue == null) {
			return false;
		}

		assert repository != null;
		return repository.uniqueField(fieldName, fieldValue.toString());
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format("The %s already exists.", fieldName);
	}
}
