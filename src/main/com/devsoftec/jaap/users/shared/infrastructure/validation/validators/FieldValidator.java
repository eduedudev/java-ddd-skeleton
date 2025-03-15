package com.devsoftec.jaap.users.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.Nullable;

import com.devsoftec.jaap.users.shared.domain.Repository;

public interface FieldValidator {
	Boolean isValid(
		String fieldName,
		HashMap<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	);

	String errorMessage(String fieldName, @Nullable String rule);
}
