package com.jaapec.tenant.shared.infrastructure.validation;

import java.util.List;
import java.util.Map;

public final class ValidationResponse {

	private final Map<String, List<String>> validationErrors;

	public ValidationResponse(Map<String, List<String>> validationErrors) {
		this.validationErrors = validationErrors;
	}

	public Boolean hasErrors() {
		return !validationErrors.isEmpty();
	}

	public Map<String, List<String>> errors() {
		return validationErrors;
	}

	public void addError(Map<String, List<String>> errors) {
		validationErrors.putAll(errors);
	}
}
