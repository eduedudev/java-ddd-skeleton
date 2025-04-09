package com.jaapec.tenant.shared.domain;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;

public final class Utils {

	private Utils() {}

	public static String dateToString(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public static String dateToString(Timestamp timestamp) {
		return dateToString(timestamp.toLocalDateTime());
	}

	public static String jsonEncode(Map<String, Serializable> map) {
		try {
			return new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Serializable> jsonDecode(String body) {
		try {
			return new ObjectMapper().readValue(body, Map.class);
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to decode JSON string", e);
		}
	}

	public static String toSnake(String text) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text);
	}

	public static String toCamel(String text) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, text);
	}

	public static String toCamelFirstLower(String text) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, text);
	}
}
