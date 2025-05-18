package com.jaapec.tenant.shared.infrastructure.validation.validators;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.annotation.Nullable;

import com.jaapec.tenant.shared.domain.Message;
import com.jaapec.tenant.shared.domain.Repository;

public final class RegexValidation implements FieldValidator {

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

		if (Objects.requireNonNull(rule).contains(":") && rule.split(":")[0].equals("regex")) {
			String regex = rule.split(":")[1];
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(fieldValue.toString());
			return matcher.matches();
		}
		return false;
	}

	@Override
	public Message errorMessage(String fieldName, @Nullable String rule) {
		return new Message("error.regex.invalid", new Object[] { fieldName });
	}
}
