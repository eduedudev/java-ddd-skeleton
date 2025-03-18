package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Repository;

public class RegexValidation implements FieldValidator {

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
		if (rule.contains(":") && rule.split(":")[0].equals("regex")) {
			String regex = rule.split(":")[1];
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(fieldValue.toString());
			return matcher.matches();
		}
		return false;
	}

	@Override
	public String errorMessage(String fieldName, @Nullable String rule) {
		return String.format("The field %s does not meet the established requirements.", fieldName);
	}
}
