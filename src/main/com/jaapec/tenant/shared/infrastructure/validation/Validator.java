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

	private final MessageTranslator translator;

	@Autowired
	public Validator(MessageTranslator translator) {
		this.translator = translator;
	}

	private static final Map<String, FieldValidator> validators = Map.ofEntries(
		Map.entry("required", new RequiredValidator()),
		Map.entry("string", new StringValidator()),
		Map.entry("not_empty", new NotEmptyValidator()),
		Map.entry("uuid", new UuidValidator()),
		Map.entry("unique", new DuplicateFieldValidator()),
		Map.entry("enum", new EnumValidator()),
		Map.entry("email", new EmailValidator()),
		Map.entry("min", new MinValidation()),
		Map.entry("max", new MaxValidation()),
		Map.entry("regex", new RegexValidation()),
		Map.entry("date", new DateValidator()),
		Map.entry("datetime", new DateTimeValidator()),
		Map.entry("double", new DoubleValidator()),
		Map.entry("bigdecimal", new BigDecimalValidator()),
		Map.entry("integer", new IntegerValidator())
	);

	public ValidationResponse validate(String input, Map<String, String> combinedRules, Repository repository)
		throws ValidatorNotExist {
		Map<String, List<String>> validationErrors = new HashMap<>();

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
				Boolean isValid = validator.isValid(entry.getKey(), jsonDecode(input), repository, ruleData);
				if (Boolean.FALSE.equals(isValid)) {
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
