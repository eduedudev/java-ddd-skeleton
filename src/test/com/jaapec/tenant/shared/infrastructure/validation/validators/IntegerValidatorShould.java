package com.jaapec.tenant.shared.infrastructure.validation.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class IntegerValidatorShould {

	private final IntegerValidator validator = new IntegerValidator();

	@Test
	void return_true_when_field_value_is_an_integer() {
		Map<String, Serializable> fields = new HashMap<>();
		fields.put("age", 25);

		Boolean result = validator.isValid("age", fields, null, null);

		assertTrue(result);
	}

	@Test
	void return_false_when_field_value_is_a_string() {
		Map<String, Serializable> fields = new HashMap<>();
		fields.put("age", "25");

		Boolean result = validator.isValid("age", fields, null, null);

		assertFalse(result);
	}

	@Test
	void return_false_when_field_value_is_a_long() {
		Map<String, Serializable> fields = new HashMap<>();
		fields.put("age", 25L);

		Boolean result = validator.isValid("age", fields, null, null);

		assertFalse(result);
	}

	@Test
	void return_false_when_field_value_is_a_double() {
		Map<String, Serializable> fields = new HashMap<>();
		fields.put("age", 25.5);

		Boolean result = validator.isValid("age", fields, null, null);

		assertFalse(result);
	}

	@Test
	void return_false_when_field_does_not_exist() {
		Map<String, Serializable> fields = new HashMap<>();

		Boolean result = validator.isValid("age", fields, null, null);

		assertFalse(result);
	}

	@Test
	void return_false_when_field_value_is_null() {
		Map<String, Serializable> fields = new HashMap<>();
		fields.put("age", null);

		Boolean result = validator.isValid("age", fields, null, null);

		assertFalse(result);
	}

	@Test
	void return_error_message_with_field_name() {
		var message = validator.errorMessage("age", null);

		assertEquals("error.integer.invalid", message.messageKey());
		assertEquals("age", message.args()[0]);
	}
}
