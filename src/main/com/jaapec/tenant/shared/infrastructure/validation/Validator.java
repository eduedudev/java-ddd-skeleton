package com.jaapec.tenant.shared.infrastructure.validation;

import static com.jaapec.tenant.shared.domain.Utils.jsonDecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jaapec.tenant.shared.domain.MessageTranslator;
import com.jaapec.tenant.shared.domain.Repository;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.infrastructure.validation.validators.*;

@Service
public final class Validator {

	@Autowired
	private MessageTranslator translator;

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
			put("integer", new IntegerValidator());
		}
	};

	public ValidationResponse validate(String input, HashMap<String, String> combinedRules, Repository repository)
		throws ValidatorNotExist {
		HashMap<String, List<String>> validationErrors = new HashMap<>();

		for (Map.Entry<String, String> entry : combinedRules.entrySet()) {
			String[] rules = entry.getValue().split("\\|");
			for (String rule : rules) {
				FieldValidator validator;
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
					existingErrors.add(
						translator.translate(
							validator.errorMessage(entry.getKey(), ruleData).messageKey(),
							validator.errorMessage(entry.getKey(), ruleData).args()
						)
					);

					validationErrors.put(entry.getKey(), existingErrors);
				}
			}
		}

		return new ValidationResponse(validationErrors);
	}
}
