package com.devsoftec.jaap.users.shared.infrastructure.validation;

import static com.devsoftec.jaap.users.shared.domain.Utils.jsonDecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devsoftec.jaap.users.shared.domain.Repository;
import com.devsoftec.jaap.users.shared.infrastructure.validation.validators.*;
import com.devsoftec.jaap.users.shared.infrastructure.validation.validators.FieldValidator;

public final class Validator {

	private static final HashMap<String, FieldValidator> validators = new HashMap<String, FieldValidator>() {
		{
			put("required", new RequiredValidator());
			put("string", new StringValidator());
			put("not_empty", new NotEmptyValidator());
			put("uuid", new UuidValidator());
			put("unique", new UniqueFieldValidator());
			put("enum", new EnumValidator());
			put("email", new EmailValidator());
			put("min", new MinValidation());
			put("max", new MaxValidation());
			put("regex", new RegexValidation());
			put("date", new DateValidator());
			put("datetime", new DateTimeValidator());
			put("double", new DoubleValidator());
			put("bigdecimal", new BigDecimalValidator());
		}
	};

	public static ValidationResponse validate(String input, HashMap<String, String> combinedRules, Repository repository)
		throws ValidatorNotExist {
		HashMap<String, List<String>> validationErrors = new HashMap<>();

		for (Map.Entry<String, String> entry : combinedRules.entrySet()) {
			String[] rules = entry.getValue().split("\\|");
			for (String rule : rules) {
				FieldValidator validator = null;
				String ruleData = null;
				if (rule.contains(":")) {
					validator = validators.get(rule.split(":")[0]);
					ruleData = rule;
				} else {
					validator = validators.get(rule);
				}

				if (null == validator) {
					throw new ValidatorNotExist(rule);
				}

				if (!validator.isValid(entry.getKey(), jsonDecode(input), repository, ruleData)) {
					List<String> existingErrors = validationErrors.getOrDefault(entry.getKey(), new ArrayList<>());
					existingErrors.add(validator.errorMessage(entry.getKey(), ruleData));

					validationErrors.put(entry.getKey(), existingErrors);
				}
			}
		}

		return new ValidationResponse(validationErrors);
	}
}
