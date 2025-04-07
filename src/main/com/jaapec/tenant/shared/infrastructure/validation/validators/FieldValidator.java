package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public interface FieldValidator {
	Boolean isValid(
		String fieldName,
		Map<String, Serializable> fields,
		@Nullable Repository repository,
		@Nullable String rule
	);

	Message errorMessage(String fieldName, @Nullable String rule);
}
